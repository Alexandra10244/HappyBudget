package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.BudgetAlreadyExistsException;
import com.hbadget.happy_budget.exceptions.BudgetNotFoundException;
import com.hbadget.happy_budget.exceptions.InsufficientFundsException;
import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.entities.User;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.repositories.BudgetRepository;
import com.hbadget.happy_budget.services.interfaces.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ObjectMapper objectMapper;

    @Override
    public BudgetDTO createBudget(BudgetDTO budgetDTO, Principal connectedUser) {
        if(budgetDTO.getBudgetCategory().equals(BudgetCategory.UNUSED)){
            throw new BudgetAlreadyExistsException("You can't add an UNUSED budget, it is generated automatically, when creating incomes!");
        }
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budgetEntity = objectMapper.convertValue(budgetDTO, Budget.class);
        Budget unusedBudget = budgetRepository.findUnusedBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("You must have incomes before creating budgets!"));

        if (budgetRepository.findBudgetByCategory(budgetDTO.getBudgetCategory().toString(), user.getId()).isEmpty()) {
            if (unusedBudget.getBudgetSum()  >= budgetEntity.getBudgetSum()) {
                unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() - budgetEntity.getBudgetSum());
                budgetRepository.save(unusedBudget);
                budgetEntity.setUser(user);
                Budget responseBudget = budgetRepository.save(budgetEntity);
                return objectMapper.convertValue(responseBudget, BudgetDTO.class);
            } else {
                throw new InsufficientFundsException("Insufficient funds for creating new budget.");
            }
        } else {
            throw new BudgetAlreadyExistsException("Budget already exists.");
        }
    }

    @Override
    public BudgetDTO updateBudget(BudgetDTO budgetDTO, Long id, Principal connectedUser) {
        if(budgetDTO.getBudgetCategory().equals(BudgetCategory.UNUSED)){
            throw new BudgetAlreadyExistsException("You can't edit an UNUSED budget, it is updated automatically, when creating incomes!");
        }
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));
        Budget unusedBudget = budgetRepository.findUnusedBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("You must have incomes before creating budgets!"));
        if (user.getId().equals(budget.getUser().getId())) {
            if (budgetDTO.getBudgetSum() > 0) {
                if(budget.getBudgetSum() > budgetDTO.getBudgetSum()){
                    unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() + ( budget.getBudgetSum() - budgetDTO.getBudgetSum()));
                    budgetRepository.save(unusedBudget);
                    budget.setBudgetSum(budgetDTO.getBudgetSum());
                }else if(budget.getBudgetSum() < budgetDTO.getBudgetSum()){
                    if(unusedBudget.getBudgetSum() >= (budgetDTO.getBudgetSum() - budget.getBudgetSum())){
                        unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() - (budgetDTO.getBudgetSum() - budget.getBudgetSum()));
                        budgetRepository.save(unusedBudget);
                        budget.setBudgetSum(budgetDTO.getBudgetSum());
                    }else{
                        throw new InsufficientFundsException("Insufficient Funds to raise budget sum!");
                    }
                }

            }
            if (budgetDTO.getBudgetCategory() != null) {
                budget.setBudgetCategory(budgetDTO.getBudgetCategory());
            }
            if (budgetDTO.getBudgetDate() != null) {
                budget.setBudgetDate(budgetDTO.getBudgetDate());
            }
            budgetRepository.save(budget);
        } else {
            throw new BudgetNotFoundException("Budget not found.");
        }

        return objectMapper.convertValue(budget, BudgetDTO.class);
    }

    @Override
    public String deleteBudget(Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));
        Budget unusedBudget = budgetRepository.findUnusedBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("You must have incomes before creating budgets!"));
        if (budgetRepository.existsById(id)) {
            unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() + budget.getBudgetSum());
            budgetRepository.save(unusedBudget);
            budgetRepository.deleteById(id);
            return "Budget deleted.";
        } else {
            throw new BudgetNotFoundException("Budget not found.");
        }
    }

    @Override
    public List<BudgetDTO> getBudgetByCategory(BudgetCategory budgetCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Budget> budgets = budgetRepository.findBudgetsByCategory(budgetCategory.toString(), user.getId());

        return budgets.stream().map(this::convertBudgetToDTO).toList();
    }


    @Override
    public List<BudgetDTO> getBudgetsForUser(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Budget> budgets = budgetRepository.findBudgetsForUser(user.getId());
        return budgets.stream().map(this::convertBudgetToDTO).toList();
    }

    private BudgetDTO convertBudgetToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setId(budget.getId());
        budgetDTO.setBudgetCategory(budget.getBudgetCategory());
        budgetDTO.setBudgetDate(budget.getBudgetDate());
        budgetDTO.setBudgetSum(budget.getBudgetSum());
        return budgetDTO;
    }
}
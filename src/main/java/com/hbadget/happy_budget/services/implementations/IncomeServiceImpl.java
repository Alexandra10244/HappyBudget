package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.BudgetNotFoundException;
import com.hbadget.happy_budget.exceptions.IncomeNotFoundException;
import com.hbadget.happy_budget.exceptions.InsufficientFundsException;
import com.hbadget.happy_budget.models.dtos.BudgetDTO;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.entities.Budget;
import com.hbadget.happy_budget.models.entities.Income;
import com.hbadget.happy_budget.models.enums.BudgetCategory;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import com.hbadget.happy_budget.repositories.BudgetRepository;
import com.hbadget.happy_budget.repositories.IncomeRepository;
import com.hbadget.happy_budget.repositories.UserRepository;
import com.hbadget.happy_budget.services.interfaces.IncomeService;
import com.hbadget.happy_budget.models.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final ObjectMapper objectMapper;
    private final BudgetRepository budgetRepository;

    @Override
    public IncomeDTO createIncome(IncomeDTO incomeDTO, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Income income = objectMapper.convertValue(incomeDTO, Income.class);
        income.setUser(user);
        Income responseIncome = incomeRepository.save(income);

        Optional<Budget> unusedBudgetOptional = budgetRepository.findUnusedBudgetForUser(user.getId());

        if (unusedBudgetOptional.isPresent()) {
            Budget unusedBudget = unusedBudgetOptional.get();
            unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() + income.getIncomeSum());
            budgetRepository.save(unusedBudget);
        } else {
            Budget unusedBudget = new Budget();
            unusedBudget.setBudgetCategory(BudgetCategory.UNUSED);
            unusedBudget.setBudgetSum(income.getIncomeSum());
            unusedBudget.setBudgetDate(LocalDateTime.now());
            unusedBudget.setUser(user);
            budgetRepository.save(unusedBudget);
        }

        return objectMapper.convertValue(responseIncome, IncomeDTO.class);
    }

    @Override
    public IncomeDTO updateIncome(IncomeDTO incomeDTO, Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IncomeNotFoundException("Income not found."));
        Budget unusedBudget = budgetRepository.findUnusedBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("Unused Budget not set"));

        if (user.getId().equals(income.getUser().getId())) {
            if (incomeDTO.getTitle() != null && !incomeDTO.getTitle().isEmpty()) {
                income.setTitle(incomeDTO.getTitle());
            }
            if (incomeDTO.getIncomeSum() != 0) {
                if (incomeDTO.getIncomeSum() >= income.getIncomeSum()) {
                    unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() + (incomeDTO.getIncomeSum() - income.getIncomeSum()));
                    income.setIncomeSum(incomeDTO.getIncomeSum());
                    budgetRepository.save(unusedBudget);
                } else {
                    if (unusedBudget.getBudgetSum() - (income.getIncomeSum() - incomeDTO.getIncomeSum()) >= 0) {
                        unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() - (income.getIncomeSum() - incomeDTO.getIncomeSum()));
                        income.setIncomeSum(incomeDTO.getIncomeSum());
                        budgetRepository.save(unusedBudget);
                    } else {
                        throw new InsufficientFundsException("Insufficient founds. Income spent! Delete expenses to update income with lower sum!");
                    }
                }

            }
            if (incomeDTO.getIncomeCategory() != null) {
                income.setIncomeCategory(incomeDTO.getIncomeCategory());
            }
            if (incomeDTO.getIncomeDate() != null) {
                income.setIncomeDate(incomeDTO.getIncomeDate());
            }
            incomeRepository.save(income);
        } else {
            throw new IncomeNotFoundException("Income not found.");
        }

        return objectMapper.convertValue(income, IncomeDTO.class);
    }

    @Override
    public String deleteIncome(Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IncomeNotFoundException("Income not found."));
        Budget unusedBudget = budgetRepository.findUnusedBudgetForUser(user.getId()).orElseThrow(() -> new BudgetNotFoundException("Unused Budget not set"));
        if (user.getId().equals(income.getUser().getId())) {
            if (unusedBudget.getBudgetSum() - income.getIncomeSum() >= 0) {
                unusedBudget.setBudgetSum(unusedBudget.getBudgetSum() - income.getIncomeSum());
                incomeRepository.deleteById(id);
            } else {
                throw new InsufficientFundsException("Insufficient founds. Income spent! Delete expenses in order to delete income!");
            }
        }
        return "Income deleted.";
    }

    @Override
    public List<IncomeDTO> getIncomeByCategory(IncomeCategory incomeCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Income> incomes = incomeRepository.findIncomesByCategory(incomeCategory.toString(), user.getId());

        return incomes.stream().map(this::convertIncomeToDTO).toList();
    }

    @Override
    public List<IncomeDTO> getAllIncomesForUser(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Income> incomes = incomeRepository.findAllIncomesForUser(user.getId());
        return incomes.stream().map(this::convertIncomeToDTO).toList();
    }

    private IncomeDTO convertIncomeToDTO(Income income) {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setId(income.getId());
        incomeDTO.setTitle(income.getTitle());
        incomeDTO.setIncomeSum(income.getIncomeSum());
        incomeDTO.setIncomeCategory(income.getIncomeCategory());
        incomeDTO.setIncomeDate(income.getIncomeDate());
        return incomeDTO;
    }
}

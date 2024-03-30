package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.IncomeNotFoundException;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.entities.Income;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import com.hbadget.happy_budget.repositories.IncomeRepository;
import com.hbadget.happy_budget.repositories.UserRepository;
import com.hbadget.happy_budget.services.interfaces.IncomeService;
import com.hbadget.happy_budget.models.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public IncomeDTO createIncome(IncomeDTO incomeDTO, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Income income = objectMapper.convertValue(incomeDTO, Income.class);
        income.setUser(user);
        Income responseIncome = incomeRepository.save(income);

        return objectMapper.convertValue(responseIncome, IncomeDTO.class);
    }

    @Override
    public IncomeDTO updateIncome(IncomeDTO incomeDTO, Long id,Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IncomeNotFoundException("Income not found."));

        if(user.getId().equals(income.getUser().getId())) {
            if (incomeDTO.getIncomeSum() != 0) {
                incomeDTO.setIncomeSum(incomeDTO.getIncomeSum());
            }

            if (incomeDTO.getIncomeCategory() != null) {
                incomeDTO.setIncomeCategory(incomeDTO.getIncomeCategory());
            }

            if (incomeDTO.getIncomeDate() != null) {
                incomeDTO.setIncomeDate(incomeDTO.getIncomeDate());
            }
        } else{
            throw new IncomeNotFoundException("Income not found.");
        }

        return objectMapper.convertValue(income, IncomeDTO.class);
    }

    @Override
    public String deleteIncome(Long id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IncomeNotFoundException("Income not found."));
        if (user.getId().equals(income.getUser().getId())) {
            incomeRepository.deleteById(id);
        }
        return "Income deleted.";
    }

    @Override
    public List<IncomeDTO> getIncomeByCategory(IncomeCategory incomeCategory, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Income> incomes = incomeRepository.findIncomeByCategory(incomeCategory,user.getId());

        if (incomes.isEmpty()) {
            return Collections.emptyList();
        }
        return incomes.stream()
                    .map(this::convertIncomeToDTO)
                .collect(Collectors.toList());
    }

    private IncomeDTO convertIncomeToDTO(Income income) {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setIncomeSum(income.getIncomeSum());
        incomeDTO.setIncomeCategory(income.getIncomeCategory());
        incomeDTO.setIncomeDate(income.getIncomeDate());
        return incomeDTO;
    }
}

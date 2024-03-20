package com.hbadget.happy_budget.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbadget.happy_budget.exceptions.IncomeNotFoundException;
import com.hbadget.happy_budget.models.dtos.IncomeDTO;
import com.hbadget.happy_budget.models.entities.Income;
import com.hbadget.happy_budget.models.enums.IncomeCategory;
import com.hbadget.happy_budget.repositories.IncomeRepository;
import com.hbadget.happy_budget.services.interfaces.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private IncomeRepository incomeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public IncomeDTO createIncome(IncomeDTO incomeDTO) {
        Income income = incomeRepository.save(objectMapper.convertValue(incomeDTO, Income.class));

        return objectMapper.convertValue(income, IncomeDTO.class);
    }

    @Override
    public IncomeDTO updateIncome(IncomeDTO incomeDTO, Long id) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IncomeNotFoundException("Income not found."));

        if (incomeDTO.getIncomeSum() != 0) {
            incomeDTO.setIncomeSum(incomeDTO.getIncomeSum());
        }

        if (incomeDTO.getIncomeCategory() != null) {
            incomeDTO.setIncomeCategory(incomeDTO.getIncomeCategory());
        }

        if (incomeDTO.getIncomeDate() != null) {
            incomeDTO.setIncomeDate(incomeDTO.getIncomeDate());
        }

        return objectMapper.convertValue(income, IncomeDTO.class);
    }

    @Override
    public String deleteIncome(Long id) {

        if (incomeRepository.existsById(id)) {
            incomeRepository.deleteById(id);
        }
        return "Income deleted.";
    }

    @Override
    public List<IncomeDTO> getIncomeByCategory(IncomeCategory incomeCategory) {
        List<Income> incomes = incomeRepository.findIncomeByCategory(incomeCategory);

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

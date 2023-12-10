package com.example.minhacarteira;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class NewSpending extends Fragment implements AutoCloseable {

    BudgetDB db;

    EditText dateEdt;
    EditText descriptionEdt;

    EditText valueEdt;

    Button btnSave;

    public NewSpending() { }

    public static NewSpending newInstance() {
        NewSpending fragment = new NewSpending();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new BudgetDB(getContext());

        View view = inflater.inflate(R.layout.fragment_new_spending, container, false);
        descriptionEdt = (EditText)view.findViewById(R.id.descricao);
        dateEdt = (EditText)view.findViewById(R.id.edt_data);
        valueEdt = (EditText)view.findViewById(R.id.edt_valor);
        btnSave = (Button)view.findViewById(R.id.btn_salvar);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Budget budget = new Budget(dateEdt.getText().toString(), valueEdt.getText().toString(), descriptionEdt.getText().toString());
                    db.insertBudget(budget);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    ListSpending listSpending = null;
                    for(Fragment f : fm.getFragments()) {
                        if(f instanceof ListSpending) {
                            listSpending = (ListSpending)f;
                            break;
                        }
                    }

                    if(listSpending != null) {
                        listSpending.searchMonthSpending();
                    }

                    clearFields();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void clearFields() {
                descriptionEdt.setText("");
                dateEdt.setText("");
                valueEdt.setText("");
            }
        });

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Ajusta o formato da data para exibir o mês e o ano
                                int adjustedMonth = monthOfYear + 1;  // Adiciona 1 ao mês, pois o DatePicker retorna de 0 a 11
                                dateEdt.setText(String.format("%02d/%02d/%04d", dayOfMonth, adjustedMonth, year));
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        return view;
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}

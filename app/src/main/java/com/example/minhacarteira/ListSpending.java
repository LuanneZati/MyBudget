package com.example.minhacarteira;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListSpending extends Fragment implements AutoCloseable {

    private TextView txtTotalValue;
    private BudgetDB db;
    private ArrayList<String> spending;

    private EditText dateEdt;
    private Button searchButton;
    private RecyclerView spendingRecyclerView;

    public ListSpending() {
        spending = new ArrayList<>();
    }

    public static ListSpending newInstance() {
        ListSpending fragment = new ListSpending();
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

        View view = inflater.inflate(R.layout.fragment_list_budget, container, false);
        dateEdt = view.findViewById(R.id.listar_edt_data);
        searchButton = view.findViewById(R.id.btn_buscar);
        spendingRecyclerView = view.findViewById(R.id.recyclerView);
        txtTotalValue = view.findViewById(R.id.txt_total_value);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setItemPrefetchEnabled(false);
        spendingRecyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMonthSpending();
            }
        });

        searchMonthSpending();

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        return view;
    }

    @SuppressLint("StringFormatInvalid")
    public void searchMonthSpending() {
        spending.clear();
        double totalValue = 0;

        // Obtém a data no formato MM/yyyy
        String selectedMonthYear = dateEdt.getText().toString();

        try {
            ArrayList<Budget> result;

            if (!TextUtils.isEmpty(selectedMonthYear)) {
                // Ajusta o formato da entrada do usuário para garantir consistência
                SimpleDateFormat inputFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
                Date selectedDate = inputFormat.parse(selectedMonthYear);

                // Formata a data de seleção para extrair apenas o mês e o ano
                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
                String formattedDate = monthYearFormat.format(selectedDate);

                // Executa a consulta SQL adaptada
                result = db.selectSpending("substr(" + DBSchema.BudgetTbl.Cols.date + ", 4, 7) = ?", new String[]{formattedDate});
            } else {
                // Se nenhuma data específica for selecionada, retorna todos os gastos
                result = db.selectSpending();
            }

            // Atualiza a lista de gastos
            for (int i = 0; i < result.size(); i++) {
                Budget a = result.get(i);
                spending.add(a.getDescription() + " - " + a.getData() + " - " + a.getValue());
                double numericValue = Double.parseDouble(a.getValue().replaceAll("[^0-9.]", ""));
                totalValue += numericValue;
            }
            txtTotalValue.setText(String.format(Locale.getDefault(), "%.2f", totalValue));

            MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getContext(), spending);
            spendingRecyclerView.setAdapter(adapter);

        } catch (NumberFormatException e) {
            Log.e("ERROR", "Erro ao converter valor para double: " + e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", "Exceção genérica ocorreu: " + e.getMessage());
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Ajusta o formato da data para exibir o mês e o ano
                        dateEdt.setText(String.format("%02d/%04d", monthOfYear + 1, year));
                    }
                },
                year, month, 1);

        datePickerDialog.show();
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}

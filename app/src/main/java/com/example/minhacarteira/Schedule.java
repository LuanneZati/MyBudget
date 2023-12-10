package com.example.minhacarteira;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {
    private ArrayList<Budget> mSpending;

    public Schedule() {
        mSpending = new ArrayList<>();
    }

    public void addSpending(Budget spending) {
        mSpending.add(spending);
    }

    public void removeSpending(Budget spending) {
        mSpending.removeIf(a -> a.getUUID().equals(spending.getUUID()));
    }

    public List<Budget> getByDate(Date date) {
        return mSpending.stream().filter(a -> a.getData().equals(date)).collect(Collectors.toList());
    }
}

package com.mezan.extreme.food;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        this.myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //Appetizer
                Appetizer appetizer = new Appetizer();
                return appetizer;
            case 1:
                //Salad
                Salad salad = new Salad();
                return salad;
            case 2:
                //Pasta
                Pasta pasta = new Pasta();
                return pasta;
            case 3:
                //Pizza
                Pizza pizza = new Pizza();
                return pizza;
            case 4:
                //Burger
                Burger burger = new Burger();
                return burger;
            case 5:
                //Chicken
                Chicken chicken = new Chicken();
                return chicken;
            case 6:
                //Beef
                Beef beef = new Beef();
                return beef;
            case 7:
                //Vegetable
                Vegetables vegetables = new Vegetables();
                return vegetables;
            case 8:
                //Rice
                Rice rice = new Rice();
                return rice;
            case 9:
                //set menu
                SetMenu setmenu = new SetMenu();
                return setmenu;
            case 10:
                //Soup
                Soup soup = new Soup();
                return soup;
            case 11:
                //Drinks
                Drinks drinks = new Drinks();
                return drinks;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}


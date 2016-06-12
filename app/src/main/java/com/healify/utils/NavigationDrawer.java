package com.healify.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.healify.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.Drawer;


public class NavigationDrawer {

    Activity activity;
    Toolbar toolbar;

    public NavigationDrawer(Activity activity, Toolbar toolbar){
        this.activity=activity;
        this.toolbar=toolbar;
    }

    public void setupDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Healify");
        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Update");
        final SecondaryDrawerItem item3 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Health state");
        final SecondaryDrawerItem item4 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("CheckOut");

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("John Smith").withEmail("testmail@gmsil.com").withIcon(activity.getResources().getDrawable(R.drawable.drawer_background))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(item3)){
//                            Intent intent = new Intent(activity, CreateEvent.class);
//                            activity.startActivityForResult(intent, 1);
                        }
                        return true;
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(item4)){
//                            Intent intent = new Intent(activity, MapActivity.class);
//                            activity.startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();

        result.setSelection(item2);
    }
}

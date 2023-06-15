<?xml version="1.0" encoding="utf-8"?>

<androidx.core.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".ui.home.HomeFragment">

        <RelativeLayout
            android:id="@+id/rl_slider"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:layout_margin="5dp">

            <androidx.viewpager.widget.ViewPager
                android:id="@+id/main_slider_view"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

            <com.zhpan.indicator.IndicatorView
                android:id="@+id/main_slider_indicator_view"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentBottom="true"
                android:layout_centerHorizontal="true"
                android:layout_marginBottom="10dp" />

        </RelativeLayout>


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:padding="10dp">


                <androidx.cardview.widget.CardView
                    android:id="@+id/card_profile"
                    android:layout_width="0dp"
                    android:layout_height="120dp"
                    android:layout_margin="5dp"
                    android:layout_weight="1"
                    app:cardCornerRadius="2dp"
                    app:cardElevation="3dp"
                    app:cardPreventCornerOverlap="false"
                    app:cardUseCompatPad
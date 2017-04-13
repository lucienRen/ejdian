package com.ej.ejdian.annotation;


import com.ej.ejdian.ui.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xubo on 2016/8/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LayoutSetting {

    int titleId() default R.layout.title_bar;

    boolean isThemeBar() default true;

}

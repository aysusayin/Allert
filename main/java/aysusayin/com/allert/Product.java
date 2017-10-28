package aysusayin.com.allert;

import android.support.annotation.NonNull;

/**
 * Created by Aysu on 7.09.2017.
 */

public class Product implements Comparable {
    private String name;
    private String content;

    public Product() {
    }

    public Product(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Product) {
            Product x = (Product) o;
            String otherName = x.getName();
            String thisName = name;
            while (otherName.length() > 0 && thisName.length() > 0) {
                if (otherName.charAt(0) == thisName.charAt(0)) {
                    otherName = otherName.substring(1);
                    thisName = thisName.substring(1);
                } else {
                    return thisName.charAt(0) - otherName.charAt(0);
                }
            }
            return 0;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            return ((Product) obj).name.equals(this.name) && ((Product) obj).content.equals(this.content);
        } else return false;
    }
}

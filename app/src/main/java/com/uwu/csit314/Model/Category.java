package com.uwu.csit314.Model;


public class Category {
    private String Name;
    private String Image;
    private String CategoryId;

    public Category() {
    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
        CategoryId = CategoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String image) {
        CategoryId = CategoryId;
    }

}
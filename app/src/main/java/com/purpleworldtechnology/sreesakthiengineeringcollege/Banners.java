package com.purpleworldtechnology.sreesakthiengineeringcollege;

public class Banners {
    int banner_id,is_visible,sub_header_visiblity;
    String banner_name,banner_data,sub_header_text,created_at,updated_at,deleted_at;
    public Banners()
    {
        // empty constructor
    }


    public Banners(int banner_id, int is_visible, int sub_header_visiblity, String banner_name, String banner_data, String sub_header_text,String created_at,String updated_at,String deleted_at) {
        this.banner_id = banner_id;
        this.is_visible = is_visible;
        this.sub_header_visiblity = sub_header_visiblity;
        this.banner_name = banner_name;
        this.banner_data = banner_data;
        this.sub_header_text = sub_header_text;

        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }

    public int getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(int is_visible) {
        this.is_visible = is_visible;
    }

    public int getSub_header_visiblity() {
        return sub_header_visiblity;
    }

    public void setSub_header_visiblity(int sub_header_visiblity) {
        this.sub_header_visiblity = sub_header_visiblity;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_data() {
        return banner_data;
    }

    public void setBanner_data(String banner_data) {
        this.banner_data = banner_data;
    }

    public String getSub_header_text() {
        return sub_header_text;
    }

    public void setSub_header_text(String sub_header_text) {
        this.sub_header_text = sub_header_text;
    }
}

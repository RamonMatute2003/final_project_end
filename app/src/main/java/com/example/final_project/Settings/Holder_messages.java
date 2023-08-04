package com.example.final_project.Settings;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;

public class Holder_messages extends RecyclerView.ViewHolder {

    TextView txt_name_user_messg, txt_time_messg, txt_name_file, txt_weight, txt_extension;
    ImageButton imgbtn_options;
    ImageView image_user, img_image_file;

    public Holder_messages(@NonNull View itemView) {
        super(itemView);

        txt_name_user_messg=(TextView) itemView.findViewById(R.id.txt_name_user_messg);
        txt_time_messg=(TextView) itemView.findViewById(R.id.txt_time_messg);
        txt_time_messg=(TextView) itemView.findViewById(R.id.txt_time_messg);
        txt_name_file=(TextView) itemView.findViewById(R.id.txt_name_file);
        txt_weight=(TextView) itemView.findViewById(R.id.txt_weight);
        txt_extension=(TextView) itemView.findViewById(R.id.txt_extension);
        imgbtn_options=itemView.findViewById(R.id.imgbtn_options);
        image_user=(ImageView) itemView.findViewById(R.id.image_user);
        img_image_file=(ImageView) itemView.findViewById(R.id.img_image_file);
    }

    public TextView getTxt_name_user_messg(){
        return txt_name_user_messg;
    }

    public void setTxt_name_user_messg(TextView txt_name_user_messg) {
        this.txt_name_user_messg = txt_name_user_messg;
    }

    public TextView getTxt_time_messg() {
        return txt_time_messg;
    }

    public void setTxt_time_messg(TextView txt_time_messg) {
        this.txt_time_messg = txt_time_messg;
    }

    public TextView getTxt_name_file() {
        return txt_name_file;
    }

    public void setTxt_name_file(TextView txt_name_file) {
        this.txt_name_file = txt_name_file;
    }

    public TextView getTxt_weight() {
        return txt_weight;
    }

    public void setTxt_weight(TextView txt_weight) {
        this.txt_weight = txt_weight;
    }

    public TextView getTxt_extension() {
        return txt_extension;
    }

    public void setTxt_extension(TextView txt_extension) {
        this.txt_extension = txt_extension;
    }

    public ImageButton getImgbtn_options() {
        return imgbtn_options;
    }

    public void setImgbtn_options(ImageButton imgbtn_options) {
        this.imgbtn_options = imgbtn_options;
    }

    public ImageView getImage_user() {
        return image_user;
    }

    public void setImage_user(ImageView image_user) {
        this.image_user = image_user;
    }

    public ImageView getImg_image_file() {
        return img_image_file;
    }

    public void setImg_image_file(ImageView img_image_file) {
        this.img_image_file = img_image_file;
    }
}

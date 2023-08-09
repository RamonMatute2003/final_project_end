package com.example.final_project.Settings;

public class Data{
    private static String email, password, career, name, phone, dni, account, birth_date, photo;
    //birth_date=fecha de nacimiento, phone=telefono,email=correo, password=contraseña, career=carrera, name=nombre, account=cuenta
    private static Integer id_career, id_user;

    public static String getPhoto() {
        return photo;
    }

    public static void setPhoto(String photo) {
        Data.photo = photo;
    }

    public Data(String email, String password, String career, String name, String phone, String dni, String birth_date){
        this.email=email;
        this.password=password;
        this.career=career;
        this.name=name;
        this.phone=phone;
        this.dni=dni;
        this.birth_date=birth_date;
    }

    public static Integer getId_user(){
        return id_user;
    }

    public static void setId_user(Integer id_user) {
        Data.id_user = id_user;
    }

    public static Integer getId_career() {
        return id_career;
    }

    public static void setId_career(Integer id_career) {
        Data.id_career = id_career;
    }

    public static String getEmail(){
        return email;
    }

    public static void setEmail(String email) {
        Data.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Data.password = password;
    }

    public static String getCareer() {
        return career;
    }

    public static void setCareer(String career) {
        Data.career = career;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Data.name = name;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Data.phone = phone;
    }

    public static String getDni() {
        return dni;
    }

    public static void setDni(String dni) {
        Data.dni = dni;
    }

    public static String getBirth_date() {
        return birth_date;
    }

    public static void setBirth_date(String birth_date) {
        Data.birth_date = birth_date;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        Data.account = account;
    }
}

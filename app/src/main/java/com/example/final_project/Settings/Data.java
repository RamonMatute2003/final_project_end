package com.example.final_project.Settings;

public class Data{

    private static String email, password, career, name, birth_date, phone, dni;//email=correo, password=contraseña, career=carrera, name=nombre, birth_date=fecha de nacimiento, phone=telefono

    public static String getEmail() {
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

    public static String getBirth_date() {
        return birth_date;
    }

    public static void setBirth_date(String birth_date) {
        Data.birth_date = birth_date;
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
}

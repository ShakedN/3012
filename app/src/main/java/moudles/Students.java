package moudles;

public class Students {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String email;
    private String phone;

    public Students(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
    public Students(){}
}

package Purpose_code_generator;

public class Register {
    private boolean status;
    //被分配给了name变量
    private String name;

    public Register() {
    }

    public Register(boolean status, String name) {
        this.status = status;
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }
}

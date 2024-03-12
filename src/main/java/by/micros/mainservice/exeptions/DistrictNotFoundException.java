package by.micros.mainservice.exeptions;

public class DistrictNotFoundException extends RuntimeException {
    public DistrictNotFoundException(Integer id) {
        super("There isn't district id " + id);
    }
}

package ru.viise.papka.entity;

public class NameSystemDriveWin implements Name {

    @Override
    public String shortName() {
        String systemDrive = System.getenv("SystemDrive");
        if(systemDrive == null)
            systemDrive = "C:";
        return systemDrive;
    }

    @Override
    public String fullName() {
        String systemDrive = System.getenv("SystemDrive");
        if(systemDrive == null)
            systemDrive = "C:";
        return systemDrive;
    }
}

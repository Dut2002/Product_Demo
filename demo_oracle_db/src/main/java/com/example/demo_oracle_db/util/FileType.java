package com.example.demo_oracle_db.util;

import lombok.Getter;

@Getter
public enum FileType {
    XLS("xls"),
    XLSX("xlsx"),
    CSV("csv"),
    PDF("pdf");
    private final String extension;
    FileType(String extension) {
        this.extension = extension;
    }

    public static FileType fromOption(String option) {
        for (FileType fileType : FileType.values()) {
            if (fileType.name().equalsIgnoreCase(option)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("Unsupported file type: " + option);
    }
}

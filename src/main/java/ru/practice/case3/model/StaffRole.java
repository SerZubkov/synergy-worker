package ru.practice.case3.model;

/** Стандартные должности (роли) — выбор по номеру в консоли; строка попадает в {@link Worker#getPosition()}. */
public enum StaffRole {

    FRONTEND("Фронтенд-разработчик"),
    BACKEND("Бэкенд-разработчик"),
    DESIGNER("Дизайнер"),
    DEVOPS("DevOps-инженер"),
    ANALYST("Аналитик"),
    QA("Тестировщик (QA)"),
    MANAGER("Менеджер"),
    /** После выбора запрашивается свой текст должности */
    OTHER("Другое");

    private final String positionTitle;

    StaffRole(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    /** Строка для поля «должность» у работника (кроме {@link #OTHER} — там подставляют ввод пользователя). */
    public String getPositionTitle() {
        return positionTitle;
    }

    public boolean isOther() {
        return this == OTHER;
    }

    public static StaffRole fromMenuIndex(int index) {
        StaffRole[] v = values();
        if (index < 1 || index > v.length) {
            return null;
        }
        return v[index - 1];
    }
}

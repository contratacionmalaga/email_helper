package local.jarios.properties;

public final class PropertyConstantes {

    public static final String CONFIG_NAME = "config.name";
    public static final String CONFIG_PATH = "config.path";
    public static final String CONFIG_PREFIJO = "config.prefijo";
    public static final String CONFIG_ESQUEMA = "config.esquema";
    public static final String CONFIG_PACKAGE_NAME = "config.package_name";

    /* Nombre de las propiedades del fichero email.properties */
    public static final String EMAIL_USER = "mail.user";
    public static final String EMAIL_PASSWORD = "mail.password";
    public static final String EMAIL_FROM = "mail.from";
    public static final String EMAIL_TO = "mail.to";

    /* Nombre de las propiedades del fichero hibernate.properties */
    public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    public static final String HIBERNATE_HIGHLIGHT_SQL = "hibernate.highlight_sql";
    public static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    public static final String HIBERNATE_JTA_PLATFORM = "hibernate.transaction.jta.platform";
    public static final String HIBERNATE_SSL = "hibernate.ssl";

    public static final String HIBERNATE_PRINCIPAL_URL = "hibernate.principal.url";
    public static final String HIBERNATE_PRINCIPAL_DRIVER = "hibernate.principal.driver";
    public static final String HIBERNATE_PRINCIPAL_DIALECT = "hibernate.principal.dialect";
    public static final String HIBERNATE_PRINCIPAL_USERNAME = "hibernate.principal.username";
    public static final String HIBERNATE_PRINCIPAL_PASSWORD = "hibernate.principal.password";

    public static final String HIBERNATE_HIKARI_MAXIMUMPOOLSIZE = "hibernate.hikari.maximumPoolSize";
    public static final String HIBERNATE_HIKARI_MINIMUMIDLE = "hibernate.hikari.minimumIdle";
    public static final String HIBERNATE_HIKARI_CONNECTIONTIMEOUT = "hibernate.hikari.connectionTimeout";
    public static final String HIBERNATE_HIKARI_IDLETIMEOUT = "hibernate.hikari.idleTimeout";
    public static final String HIBERNATE_HIKARI_MAXLIFETIME = "hibernate.hikari.maxLifetime";
    public static final String HIBERNATE_HIKARI_POOLNAME = "hibernate.hikari.poolName";

    private PropertyConstantes() { }

}

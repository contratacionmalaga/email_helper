package local.jarios.exceptions;

import org.hibernate.HibernateException;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
public class MiRepositoryException extends Exception {

    public MiRepositoryException(HibernateException ex) {

        super(ex);
    }
}

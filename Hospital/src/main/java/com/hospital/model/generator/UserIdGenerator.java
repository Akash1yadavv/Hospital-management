package com.hospital.model.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.hospital.exception.GeneralException;

public class UserIdGenerator implements IdentifierGenerator{
	@Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		

        String prefix = "H";
        

        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection();){
        	
            Statement statement=connection.createStatement();

            ResultSet rs=statement.executeQuery("call GetUsersSeqNextVal()");

            if(rs.next())
            {
                int id=rs.getInt(1);
                String generatedId = prefix + id;
                return generatedId;
            }
        } catch (SQLException e) {
            throw new GeneralException(e.getMessage());
        }

        return null;
    }

}

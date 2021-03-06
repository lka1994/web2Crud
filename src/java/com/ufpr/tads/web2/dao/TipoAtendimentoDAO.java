package com.ufpr.tads.web2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ufpr.tads.web2.beans.TipoAtendimento;

public class TipoAtendimentoDAO {
	private final String SELECT_ALL = "SELECT * FROM tb_tipo_atendimento;";
	private final String SELECT = "SELECT * FROM tb_tipo_atendimento WHERE id_tipo_atendimento = ?;";
	Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    public List<TipoAtendimento> listarTiposAtendimentos() throws InstantiationException, IllegalAccessException, SQLException {
	    List<TipoAtendimento> lista = new ArrayList<TipoAtendimento>();
    	try {
	        con = new ConnectionFactory().getConnection();
	        stmt = con.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();
            while(rs.next()){
            	TipoAtendimento t = new TipoAtendimento();
                t.setIdTipoAtendimento((rs.getInt(1)));
                t.setNomeTipoAtendimento((rs.getString(2)));
                lista.add(t);
            }
            rs.close();
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            con.close();
        }
    }
    
    public TipoAtendimento buscarTipoAtendimento(int idTipoAtendimento) throws InstantiationException, IllegalAccessException, SQLException {
		TipoAtendimento pd = new TipoAtendimento();
		try {
	        con = new ConnectionFactory().getConnection();
	        stmt = con.prepareStatement(SELECT);
            stmt.setInt(1, idTipoAtendimento);
            rs = stmt.executeQuery();
            while(rs.next()){
                pd.setIdTipoAtendimento((rs.getInt(1)));
                pd.setNomeTipoAtendimento((rs.getString(2)));
            }
            rs.close();
            return pd;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            con.close();
        }
	}

    
}

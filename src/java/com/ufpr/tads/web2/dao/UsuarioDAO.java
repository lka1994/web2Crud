/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.dao;

import com.ufpr.tads.web2.beans.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author luck
 */
public class UsuarioDAO {
	private final String VERIFY_LOGIN = "SELECT * FROM tb_usuario WHERE login_usuario = ? and senha_usuario = ?;";
	private final String SELECT = "SELECT * FROM tb_usuario WHERE id_usuario = ?;";
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    public UsuarioDAO() {
    }
    
    public Usuario verificaLogin(String login, String senha) throws ClassNotFoundException, SQLException, IOException, InstantiationException, IllegalAccessException{
        Usuario p = new Usuario();
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(VERIFY_LOGIN);
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();
            while(rs.next()){
                p.setId(rs.getInt(1));
                p.setNome(rs.getString(4));
            }
            rs.close();
            return p;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            con.close();
        }
    }
    
    public Usuario buscarUsuario(int id) {
    	Usuario aux = new Usuario();
            try {
                con = new ConnectionFactory().getConnection();
                stmt = con.prepareStatement(SELECT);
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    aux.setId(rs.getInt(1));
                    aux.setNome(rs.getString(2));
                    }
                rs.close();
                return aux;
            }catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                try {con.close();} catch (SQLException e) {}
            }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.servlets;

import com.mysql.cj.util.StringUtils;
import com.ufpr.tads.web2.beans.Cidade;
import com.ufpr.tads.web2.beans.Cliente;
import com.ufpr.tads.web2.beans.Estado;
import com.ufpr.tads.web2.facade.ClienteFacade;
import com.ufpr.tads.web2.facade.EnderecoFacade;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luck
 */
@WebServlet(name = "ClientesServlet", urlPatterns = {"/ClientesServlet"})
public class ClientesServlet extends BeanServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, ParseException {
        
    HttpSession session = request.getSession();

    if (session == null || session.getAttribute("user") == null) {

        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        request.setAttribute("msg", "Usu�rio deve se autenticar para acessar o sistema.");
        rd.forward(request, response);
        
    }else {
    
        //Início Controller
        
        //Variaveis de controle
        String action = request.getParameter("action");
        RequestDispatcher rd = null;
        int id = 0;
        List<Cliente> lista = null;
        Cliente c = null;
        
        //Identificação da action
        if(!StringUtils.isNullOrEmpty(action)){
            switch(action){

                //Listar clientes do banco de dados
                case "list":
				try {
					lista = ClienteFacade.selectAll();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    rd = request.getRequestDispatcher("clientesListar.jsp");
                    request.setAttribute("lista", lista);
                    rd.forward(request, response);                
                    break;

                //Visualizar informações de um cliente
                case "show":
                    //Busca id do cliente a ser visualizado no parametro da página
                    id = parseInt((String)request.getParameter("id"));
                    if( id > 0){
                        Cliente cliente = ClienteFacade.select(id);
                        
                        try {
							//Carregar lista de estados
							List<Estado> estados = EnderecoFacade.listarEstados();
							//Carregar cidade do cliente
							Cidade cidade = EnderecoFacade.buscarCidade(cliente.getCidadeCliente());
							
							rd = request.getRequestDispatcher("clientesForm.jsp");
							request.setAttribute("estados", estados);
							request.setAttribute("estado", (cidade.getIdEstado()-1));
							request.setAttribute("cidade", cidade);
							request.setAttribute("visualizar", true);
							request.setAttribute("cliente", cliente);
							rd.forward(request, response);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    break;

                //Ir para tela de alteração de informações de um cliente
                case "formUpdate":
                    //Busca id do cliente a ser visualizado no parametro da página
                    id = parseInt((String)request.getParameter("id"));
                    if( id > 0){
                        Cliente cliente = ClienteFacade.select(id);
                        
						try {
							//Carregar lista de estados
							List<Estado> estados = EnderecoFacade.listarEstados();
							//Carregar cidade do cliente
							Cidade cidade = EnderecoFacade.buscarCidade(cliente.getCidadeCliente());
							
							rd = request.getRequestDispatcher("clientesForm.jsp");
							request.setAttribute("estados", estados);
							request.setAttribute("estado", (cidade.getIdEstado()-1));
							request.setAttribute("cidade", cidade);
							request.setAttribute("alterar", true);
							request.setAttribute("cliente", cliente);
							rd.forward(request, response);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}                    
                    }
                    break;

                //Remover um cliente
                case "remove":
                    //Busca id do cliente a ser removido no parametro da página
                    id = parseInt((String)request.getParameter("id"));
                    if( id > 0){
                        try {
							ClienteFacade.delete(id);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        rd = request.getRequestDispatcher("ClientesServlet?action=list");
                        rd.forward(request, response);  
                    }
                    break;

                //Atualizar informações de um cliente
                case "update":
                    //Preencher dados do cliente no enviados pelo formulário
                    c = super.fillCliente(request);
                    ClienteFacade.update(c);
                    rd = request.getRequestDispatcher("ClientesServlet?action=list");
                    rd.forward(request, response);
                    break;

                //Ir para tela de inserção de um novo cliente
                case "formNew":
                    //Carregar lista de estados
				List<Estado> estados;
				try {
					estados = EnderecoFacade.listarEstados();
					rd = request.getRequestDispatcher("clientesForm.jsp");
					request.setAttribute("estados", estados);
					rd.forward(request, response);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    
                    break;

                //INserir um novo cliente
                case "new":
                    //Preencher dados do cliente no enviados pelo formulário
                    c = super.fillCliente(request);
				try {
					ClienteFacade.insert(c);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    rd = request.getRequestDispatcher("ClientesServlet?action=list");
                    rd.forward(request, response);
                    break;

                default:
                    break;
            }            
        }else{
            //Por default, a controller lista os clientes
            try {
				lista = ClienteFacade.selectAll();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            rd = request.getRequestDispatcher("clientesListar.jsp");
            request.setAttribute("lista", lista);
            rd.forward(request, response);
        }
        
        //Fim Controller
        
        }
    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ClientesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

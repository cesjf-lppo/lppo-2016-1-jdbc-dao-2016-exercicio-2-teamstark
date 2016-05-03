package br.cesjf.lppo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aluno
 */
public class AtividadeDAOPrep {
    private PreparedStatement operacaoListarTodos;
    private PreparedStatement operacaoCriar;
    private PreparedStatement operacaoExcluirPorId;
    public AtividadeDAOPrep() throws Exception{
        try{
           operacaoListarTodos = ConexaoJDBC.getInstance().prepareStatement("SELECT * FROM atividade");
           operacaoCriar = ConexaoJDBC.getInstance().prepareStatement("INSERT INTO atividade(nome, endereco) VALUES(?,?)", new String[]{"id"});
           operacaoExcluirPorId = ConexaoJDBC.getInstance().prepareStatement("DELETE FROM atividade WHERE id = ?");
        }catch (SQLException ex){
            Logger.getLogger(AtividadeDAOPrep.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);   
        }
    }
        
    public List<Atividade> listaTodos() throws Exception {
          
        List<Atividade> todos = new ArrayList<>();
        
        try {
            ResultSet resultado = operacaoListarTodos.executeQuery();
            while (resultado.next()) {
                Atividade ativ = new Atividade();
                ativ.setId(resultado.getLong("id"));
                ativ.setFuncionario(resultado.getString("funcionario"));
                ativ.setDescricao(resultado.getString("descricao"));
                ativ.setTipo(resultado.getString("tipo"));
                todos.add(ativ);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AtividadeDAOPrep.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }

        return todos;
    }

    public void criar(Atividade novaAtiv) throws Exception {
        try {
            System.out.println("Antes de criar: "+novaAtiv);
        
            operacaoCriar.setString(1, novaAtiv.getFuncionario());
            operacaoCriar.setString(2, novaAtiv.getDescricao());
            operacaoCriar.executeUpdate();
            ResultSet keys = operacaoCriar.getGeneratedKeys();
            if(keys.next()){
                novaAtiv.setId(keys.getLong(1));
            }
            System.out.println("Depois de criar: "+novaAtiv);
        }
        catch (SQLException ex) {
            Logger.getLogger(AtividadeDAOPrep.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }
    }

    public void excluirPorId(long id) throws Exception {
        try {
            //Connection conexao = ConexaoJDBC.getInstance();
            //Statement operacao = conexao.createStatement();
            operacaoExcluirPorId.setLong(1, id);
            operacaoExcluirPorId.executeUpdate();
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(AtividadeDAOPrep.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }
    }
    public void excluir(Atividade ativ) throws Exception{
        excluirPorId(ativ.getId());
    }
    public void salvar(Atividade ativ) throws Exception{
        Connection conexao = ConexaoJDBC.getInstance();
        Statement operacao = conexao.createStatement();
        try{
            operacao.executeUpdate(String.format("UPDATE estabelecimento SET funcionario='%s', descricao='%s', tipo='%s' WHERE id=%d", ativ.getFuncionario(), ativ.getDescricao(), ativ.getTipo(), ativ.getId()));
        }catch(SQLException ex){
            throw new Exception(ex);
        }
    }
    
    
    
    
    
    public Atividade buscaPorId(Long id) throws Exception {
        Atividade ativ = null;
        Connection conexao;
        try {
            conexao = ConexaoJDBC.getInstance();
            Statement operacao = conexao.createStatement();
            ResultSet resultado = operacao.executeQuery(String.format("SELECT * FROM atividade WHERE id=%d",id));
            if (resultado.next()) {
                ativ = new Atividade();
                ativ.setId(resultado.getLong("id"));
                ativ.setFuncionario(resultado.getString("funcionario"));
                ativ.setDescricao(resultado.getString("descricao"));
                ativ.setTipo(resultado.getString("tipo"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AtividadeDAOPrep.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }

        return ativ;
    }
    
    
    
    
    
    
}

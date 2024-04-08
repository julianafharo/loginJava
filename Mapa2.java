package com.mycompany.mapa2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Mapa2 extends JFrame {

    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JTextField campoNome;
    private JTextField campoNovoLogin;
    private JPasswordField campoNovaSenha;
    private JTextField campoEmail;

    public Mapa2() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Login e Cadastro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel labelLogin = new JLabel("Login:");
        JLabel labelSenha = new JLabel("Senha:");

        campoLogin = new JTextField(20);
        campoSenha = new JPasswordField(20);

        JButton botaoEntrar = new JButton("Entrar");
        JButton botaoCadastrar = new JButton("Cadastrar Novo Usuário");

        panel.add(labelLogin);
        panel.add(campoLogin);
        panel.add(labelSenha);
        panel.add(campoSenha);
        panel.add(botaoEntrar);
        panel.add(botaoCadastrar);

        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = campoLogin.getText();
                String senha = new String(campoSenha.getPassword());
                if (login.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(Mapa2.this, "Preencha todos os campos.");
                } else {
                    autenticarUsuario(login, senha);
                }
            }
        });

        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCadastroUsuario();
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);

        JButton botaoFechar = new JButton("Fechar");
        botaoFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        getContentPane().add(botaoFechar, BorderLayout.SOUTH);
    }

    private void autenticarUsuario(String login, String senha) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mapa", "root", "12345678");
            String sql = "SELECT id, nome, login, senha, email FROM usuario WHERE login = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, senha);

            if (stmt.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "Acesso Autorizado");
            } else {
                JOptionPane.showMessageDialog(this, "Acesso Negado");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao autenticar usuário. Verifique os dados e tente novamente.");
        }
    }

    private void abrirTelaCadastroUsuario() {
        JFrame telaCadastro = new JFrame("Cadastro de Novo Usuário");
        telaCadastro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        telaCadastro.setSize(400, 200);
        telaCadastro.setLocationRelativeTo(this);

        JPanel panelCadastro = new JPanel();
        panelCadastro.setLayout(new GridLayout(6, 2));

        JLabel labelNome = new JLabel("Nome:");
        JLabel labelNovoLogin = new JLabel("Novo Login:");
        JLabel labelNovaSenha = new JLabel("Nova Senha:");
        JLabel labelEmail = new JLabel("Email:");

        campoNome = new JTextField(20);
        campoNovoLogin = new JTextField(20);
        campoNovaSenha = new JPasswordField(20);
        campoEmail = new JTextField(20);

        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoVoltar = new JButton("Voltar");

        panelCadastro.add(labelNome);
        panelCadastro.add(campoNome);
        panelCadastro.add(labelNovoLogin);
        panelCadastro.add(campoNovoLogin);
        panelCadastro.add(labelNovaSenha);
        panelCadastro.add(campoNovaSenha);
        panelCadastro.add(labelEmail);
        panelCadastro.add(campoEmail);
        panelCadastro.add(botaoCadastrar);
        panelCadastro.add(botaoVoltar);

        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoNome.getText();
                String novoLogin = campoNovoLogin.getText();
                String novaSenha = new String(campoNovaSenha.getPassword());
                String email = campoEmail.getText();

                if (nome.isEmpty() || novoLogin.isEmpty() || novaSenha.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(Mapa2.this, "Preencha todos os campos.");
                } else {
                    cadastrarNovoUsuario(nome, novoLogin, novaSenha, email);
                }
            }
        });

        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                telaCadastro.dispose();
            }
        });

        telaCadastro.add(panelCadastro);
        telaCadastro.setVisible(true);
    }

    private void cadastrarNovoUsuario(String nome, String login, String senha, String email) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mapa", "root", "12345678");
            String sql = "INSERT INTO usuario (nome, login, senha, email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, login);
            stmt.setString(3, senha);
            stmt.setString(4, email);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário. Verifique os dados e tente novamente.");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário. Verifique os dados e tente novamente.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Mapa2 mapa2 = new Mapa2();
                mapa2.setVisible(true);
            }
        });
    }
}


package com.mycompany.login;

import com.formdev.flatlaf.FlatLightLaf;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Ventanaprincipal extends JFrame {

    private BufferedImage fondoImagen;
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    // Componentes Login
    private JTextField loginUsuarioField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    private JButton toRegisterButton;
    private JLabel loginStatusLabel;

    // Componentes Registro
    private JTextField regUsuarioField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private JButton registerButton;
    private JButton toLoginButton;
    private JLabel regStatusLabel;

    // Panel Usuario Verificado
    private JLabel usuarioVerificadoLabel;
    private JButton continuarButton;

    private static final String USUARIOS_TXT = "usuarios.txt";


    public Ventanaprincipal() {
        setTitle("Sistema de Login - Registro");
        setSize(420, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cargarImagenFondo();
        initComponents();
    }

    private void cargarImagenFondo() {
        try {
            fondoImagen = ImageIO.read(new File("fondo.jpg"));
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
            fondoImagen = null;
        }
    }

    class PanelConFondo extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondoImagen != null) {
                g.drawImage(fondoImagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private JPanel crearLoginPanel() {
        JPanel panel = new PanelConFondo();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0,0,0,150));
                g.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
                super.paintComponent(g);
            }
        };
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Iniciar Sesión"));
        ((TitledBorder)inputPanel.getBorder()).setTitleColor(Color.WHITE);

        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setForeground(Color.WHITE);
        loginUsuarioField = new JTextField();
        loginUsuarioField.setMaximumSize(new Dimension(Integer.MAX_VALUE,28));
        loginUsuarioField.setOpaque(false);
        loginUsuarioField.setForeground(Color.WHITE);
        loginUsuarioField.setBackground(new Color(0,0,0,150));
        loginUsuarioField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JLabel labelPassword = new JLabel("Contraseña:");
        labelPassword.setForeground(Color.WHITE);
        loginPasswordField = new JPasswordField();
        loginPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE,28));
        loginPasswordField.setOpaque(false);
        loginPasswordField.setForeground(Color.WHITE);
        loginPasswordField.setBackground(new Color(0,0,0,150));
        loginPasswordField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        inputPanel.add(labelUsuario);
        inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
        inputPanel.add(loginUsuarioField);
        inputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        inputPanel.add(labelPassword);
        inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
        inputPanel.add(loginPasswordField);

        loginButton = new JButton("Iniciar Sesión");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(140, 35));
        loginButton.addActionListener(e -> autenticarUsuario());

        toRegisterButton = new JButton("Crear nuevo usuario");
        toRegisterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toRegisterButton.setPreferredSize(new Dimension(160, 30));
        toRegisterButton.addActionListener(e -> cardLayout.show(cardsPanel, "registro"));

        loginStatusLabel = new JLabel(" ");
        loginStatusLabel.setForeground(Color.GREEN);
        loginStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(toRegisterButton);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(loginStatusLabel);
        return panel;
    }

    private JPanel crearRegistroPanel() {
        JPanel panel = new PanelConFondo();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel inputPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0,0,0,150));
                g.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
                super.paintComponent(g);
            }
        };
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Crear Usuario"));
        ((TitledBorder)inputPanel.getBorder()).setTitleColor(Color.WHITE);

        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setForeground(Color.WHITE);
        regUsuarioField = new JTextField();
        regUsuarioField.setMaximumSize(new Dimension(Integer.MAX_VALUE,28));
        regUsuarioField.setOpaque(false);
        regUsuarioField.setForeground(Color.WHITE);
        regUsuarioField.setBackground(new Color(0,0,0,150));
        regUsuarioField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JLabel labelPassword = new JLabel("Contraseña:");
        labelPassword.setForeground(Color.WHITE);
        regPasswordField = new JPasswordField();
        regPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE,28));
        regPasswordField.setOpaque(false);
        regPasswordField.setForeground(Color.WHITE);
        regPasswordField.setBackground(new Color(0,0,0,150));
        regPasswordField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        JLabel labelConfirmPassword = new JLabel("Confirmar Contraseña:");
        labelConfirmPassword.setForeground(Color.WHITE);
        regConfirmPasswordField = new JPasswordField();
        regConfirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE,28));
        regConfirmPasswordField.setOpaque(false);
        regConfirmPasswordField.setForeground(Color.WHITE);
        regConfirmPasswordField.setBackground(new Color(0,0,0,150));
        regConfirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        inputPanel.add(labelUsuario);
        inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
        inputPanel.add(regUsuarioField);
        inputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        inputPanel.add(labelPassword);
        inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
        inputPanel.add(regPasswordField);
        inputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        inputPanel.add(labelConfirmPassword);
        inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
        inputPanel.add(regConfirmPasswordField);

        registerButton = new JButton("Crear Usuario");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setPreferredSize(new Dimension(140, 35));
        registerButton.addActionListener(e -> registrarUsuario());

        toLoginButton = new JButton("Volver a Login");
        toLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toLoginButton.setPreferredSize(new Dimension(140, 30));
        toLoginButton.addActionListener(e -> cardLayout.show(cardsPanel, "login"));

        regStatusLabel = new JLabel(" ");
        regStatusLabel.setForeground(Color.GREEN);
        regStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(toLoginButton);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(regStatusLabel);
        return panel;
    }

    private JPanel crearUsuarioVerificadoPanel(String usuario) {
        JPanel panel = new PanelConFondo();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        usuarioVerificadoLabel = new JLabel("Usuario " + usuario + " inicio sesión correctamente.");
        usuarioVerificadoLabel.setForeground(Color.WHITE);
        usuarioVerificadoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usuarioVerificadoLabel.setFont(new Font("Arial", Font.BOLD, 16));

        continuarButton = new JButton("Continuar al segundo paso");
        continuarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continuarButton.setPreferredSize(new Dimension(180,40));
        continuarButton.addActionListener(e -> {
            Clave claveWindow = new Clave();
            claveWindow.setVisible(true);
            this.dispose(); // Cerrar ventana principal
        });

        panel.add(Box.createVerticalGlue());
        panel.add(usuarioVerificadoLabel);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(continuarButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        cardsPanel.add(crearLoginPanel(), "login");
        cardsPanel.add(crearRegistroPanel(), "registro");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardsPanel, BorderLayout.CENTER);
    }

    private boolean usuarioExiste(String usuario) {
        List<String[]> usuarios = leerUsuarios();
        for (String[] u : usuarios) {
            if (u[0].equalsIgnoreCase(usuario)) return true;
        }
        return false;
    }

    private List<String[]> leerUsuarios() {
        List<String[]> usuarios = new ArrayList<>();
        File f = new File(USUARIOS_TXT);
        if (!f.exists()) return usuarios;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    usuarios.add(new String[] {partes[0], partes[1]});
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return usuarios;
    }

    private void agregarUsuario(String usuario, String password) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USUARIOS_TXT,true))) {
            bw.write(usuario + ";" + password);
            bw.newLine();
        }
    }

    private void registrarUsuario() {
        String usuario = regUsuarioField.getText().trim();
        String pass = new String(regPasswordField.getPassword());
        String confirmPass = new String(regConfirmPasswordField.getPassword());

        if (usuario.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            regStatusLabel.setText("Todos los campos son obligatorios.");
            regStatusLabel.setForeground(Color.RED);
            return;
        }
        if (!pass.equals(confirmPass)) {
            regStatusLabel.setText("Las contraseñas no coinciden.");
            regStatusLabel.setForeground(Color.RED);
            return;
        }
        if (usuarioExiste(usuario)) {
            regStatusLabel.setText("El usuario ya existe.");
            regStatusLabel.setForeground(Color.RED);
            return;
        }
        try {
            agregarUsuario(usuario, pass);
            regStatusLabel.setText("Usuario creado correctamente!");
            regStatusLabel.setForeground(Color.GREEN);
            regUsuarioField.setText("");
            regPasswordField.setText("");
            regConfirmPasswordField.setText("");
        } catch (IOException e) {
            regStatusLabel.setText("Error al guardar el usuario.");
            regStatusLabel.setForeground(Color.RED);
        }
    }

    private void autenticarUsuario() {
        String usuario = loginUsuarioField.getText().trim();
        String pass = new String(loginPasswordField.getPassword());

        List<String[]> usuarios = leerUsuarios();
        boolean encontrado = false;
        for (String[] u : usuarios) {
            if (u[0].equalsIgnoreCase(usuario) && u[1].equals(pass)) {
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            loginStatusLabel.setText("Usuario verificado.");
            loginStatusLabel.setForeground(Color.GREEN);
            // Mostrar panel de usuario verificado con opción de continuar
            cardsPanel.add(crearUsuarioVerificadoPanel(usuario), "verificado");
            cardLayout.show(cardsPanel, "verificado");
        } else {
            loginStatusLabel.setText("Usuario o contraseña incorrectos.");
            loginStatusLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar FlatLaf: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(() -> new Ventanaprincipal().setVisible(true));
    }
}


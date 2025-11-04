package com.mycompany.clave;

import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.Authenticator;

import java.util.Properties;
import java.util.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Clave extends JFrame {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    private JTextField correoField;
    private JButton enviarButton;
    private JTextArea statusArea;

    public Clave() {
        setTitle("Registro - Enviar Código de Verificación");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5,5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ingrese su correo electrónico"));

        correoField = new JTextField();
        inputPanel.add(new JLabel("Correo electrónico:"));
        inputPanel.add(correoField);

        enviarButton = new JButton("Enviar Código");
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCodigo();
            }
        });

        statusArea = new JTextArea(5, 30);
        statusArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(statusArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(enviarButton, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        add(panel);
    }

    public static String generarCodigo(int longitud) {
        StringBuilder codigo = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(CARACTERES.length());
            codigo.append(CARACTERES.charAt(indice));
        }
        return codigo.toString();
    }

    public void enviarCorreo(String destinatario, String asunto, String mensajeTexto) {
        final String remitente = "ejemplocodigos@gmail.com"; 
        final String password = "dumt rccy xeiy igxx"; 

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remitente, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensajeTexto);

            Transport.send(message);
            statusArea.append("Correo enviado correctamente a: " + destinatario + "\n");
        } catch (MessagingException e) {
            e.printStackTrace();
            statusArea.append("Error al enviar correo: " + e.getMessage() + "\n");
        }
    }

    private void enviarCodigo() {
        String correoDestino = correoField.getText().trim();
        if (correoDestino.isEmpty() || !correoDestino.contains("@")) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un correo válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String codigoVerificacion = generarCodigo(8);
        String asunto = "Tu código de verificación";
        String mensaje = "Tu código de verificación es: " + codigoVerificacion;

        enviarCorreo(correoDestino, asunto, mensaje);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Clave().setVisible(true);
        });
    }
}

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class StudentRecordSystem {
    
    // Modern Color Scheme
    static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    static final Color DARK_BG = new Color(20, 25, 40);
    static final Color CARD_BG = new Color(30, 40, 60);
    static final Color TEXT_PRIMARY = Color.WHITE;
    static final Color HOVER_COLOR = new Color(52, 152, 219);

    static class Student {
        String name;
        int rollNo;
        int registrationNumber;
        String course;
        float cgpa;

        Student(String name, int rollNo, int registrationNumber, String course, float cgpa) {
            this.name = name;
            this.rollNo = rollNo;
            this.registrationNumber = registrationNumber;
            this.course = course;
            this.cgpa = cgpa;
        }

        @Override
        public String toString() {
            return rollNo + " | " + name + " | Reg#: " + registrationNumber + " | Course: " + course + " | CGPA: " + cgpa;
        }
    }


    static Student[] students = new Student[1000];
    static int studentCount = 0;

    static Student[] recentStack = new Student[500];
    static int top = -1;

    static Student[] examQueue = new Student[500];
    static int front = 0, rear = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRecordSystem::buildGUI);
    }

    private static void buildGUI() {
        JFrame frame = new JFrame("Student Record Management System");
        frame.setSize(1300, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(DARK_BG);
        frame.setIconImage(new javax.swing.ImageIcon().getImage());

        // ========== HEADER PANEL ==========
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_BLUE, 
                                                          getWidth(), 0, ACCENT_PURPLE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRect(0, getHeight()-5, getWidth(), 5);
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(100, 80));
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        JLabel heading = new JLabel("STUDENT RECORD MANAGEMENT");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(TEXT_PRIMARY);
        
        JLabel subtitle = new JLabel("Professional Database & Analytics System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(200, 220, 240));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(heading);
        titlePanel.add(subtitle);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        frame.add(headerPanel, BorderLayout.NORTH);

        // ========== MAIN CONTENT PANEL ==========
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(DARK_BG);

        // ========== SIDEBAR - INTERACTIVE CATEGORIES ==========
        JPanel sidebarPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        sidebarPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                new EmptyBorder(15, 15, 15, 15)));
        sidebarPanel.setBackground(new Color(25, 35, 55));
        sidebarPanel.setPreferredSize(new Dimension(180, 400));
        
        // Category mapping: [name, color, button indices]
        String[] categories = {"ALL", "STUDENT DATA", "MANAGEMENT", "ANALYTICS", "EXAM", "TOOLS"};
        Color[] catColors = {ACCENT_PURPLE, new Color(52, 152, 219), new Color(46, 204, 113), 
                             new Color(155, 89, 182), new Color(230, 126, 34), new Color(149, 165, 166)};
        
        JButton[] catButtons = new JButton[categories.length];
        JPanel centerPanel = new JPanel(new GridLayout(3, 4, 12, 12));
        centerPanel.setBackground(DARK_BG);
        
        String[] labels = {
                "[+] Add", "[ALL] View All", "[SEARCH] Find", "[SORT] CGPA",
                "[TOP] Top 3", "[DELETE] Remove", "[RECENT] Stack", "[EXAM] Register",
                "[QUEUE] Exam Q", "[GRAPH] Chart", "[STATS] Board", "[EXPORT] Save"
        };
        
        // Assign buttons to categories: 0:Mgmt, 1:Data, 2:Data, 3:Data, 4:Analytics, 5:Mgmt, 
        //                                 6:Data, 7:Exam, 8:Exam, 9:Analytics, 10:Analytics, 11:Tools
        int[] buttonCategories = {1, 1, 1, 1, 3, 1, 1, 4, 4, 3, 3, 5};
        
        Component[] buttons = new Component[labels.length];
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = createInteractiveButton(labels[i], catColors[buttonCategories[i]]);
            centerPanel.add(buttons[i]);
        }
        
        // Create clickable category buttons
        for (int i = 0; i < categories.length; i++) {
            final int catIndex = i;
            JButton catBtn = new JButton(categories[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Color bgColor = getModel().isArmed() ? catColors[catIndex].brighter() : new Color(35, 50, 75);
                    g2d.setColor(bgColor);
                    g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2d.setColor(catColors[catIndex]);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    
                    super.paintComponent(g);
                }
            };
            catBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            catBtn.setForeground(catColors[i]);
            catBtn.setContentAreaFilled(false);
            catBtn.setBorderPainted(false);
            catBtn.setFocusPainted(false);
            catBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            final int finalI = i;
            catBtn.addActionListener(e -> {
                // Filter/highlight buttons based on category
                for (int j = 0; j < buttons.length; j++) {
                    if (finalI == 0) { // Show ALL
                        buttons[j].setEnabled(true);
                    } else {
                        buttons[j].setEnabled(buttonCategories[j] == finalI);
                    }
                }
                // Highlight selected category
                for (JButton b : catButtons) {
                    b.setOpaque(false);
                }
                catBtn.setOpaque(true);
                catBtn.setBackground(new Color(45, 65, 95));
            });
            
            catButtons[i] = catBtn;
            sidebarPanel.add(catBtn);
        }
        
        // Select ALL by default
        catButtons[0].doClick();
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);

        // ========== BOTTOM PANEL - STATS ==========
        JPanel bottomPanel = createStatsPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ========== BUTTON ACTIONS ==========
        ((JButton) buttons[0]).addActionListener(e -> showAddForm());
        ((JButton) buttons[1]).addActionListener(e -> showAllStudents());
        ((JButton) buttons[2]).addActionListener(e -> searchByRegNumber());
        ((JButton) buttons[3]).addActionListener(e -> sortByCGPA());
        ((JButton) buttons[4]).addActionListener(e -> showTop3Students());
        ((JButton) buttons[5]).addActionListener(e -> deleteByRegNumber());
        ((JButton) buttons[6]).addActionListener(e -> showRecentAdditions());
        ((JButton) buttons[7]).addActionListener(e -> examRegistration());
        ((JButton) buttons[8]).addActionListener(e -> showExamQueue());
        ((JButton) buttons[9]).addActionListener(e -> showBarGraph());
        ((JButton) buttons[10]).addActionListener(e -> showStatsDashboard());
        ((JButton) buttons[11]).addActionListener(e -> exportData());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    static JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 35, 50), 
                                                          0, getHeight(), new Color(30, 45, 70));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Top border
                g2d.setColor(ACCENT_PURPLE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, 0, getWidth(), 0);
            }
        };
        
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
        statsPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        statsPanel.setPreferredSize(new Dimension(100, 150));
        statsPanel.setOpaque(false);
        
        // Create stat cards with fixed sizing
        JPanel stat1 = createEnhancedStatCard("Total Students", String.valueOf(studentCount), new Color(52, 152, 219));
        JPanel stat2 = createEnhancedStatCard("Exam Queue", String.valueOf(rear - front), new Color(155, 89, 182));
        JPanel stat3 = createEnhancedStatCard("Recent Added", String.valueOf(top + 1), new Color(46, 204, 113));
        JPanel stat4 = createEnhancedStatCard("System Status", "ACTIVE", new Color(241, 196, 15));
        
        statsPanel.add(Box.createHorizontalStrut(10));
        statsPanel.add(stat1);
        statsPanel.add(Box.createHorizontalStrut(15));
        statsPanel.add(stat2);
        statsPanel.add(Box.createHorizontalStrut(15));
        statsPanel.add(stat3);
        statsPanel.add(Box.createHorizontalStrut(15));
        statsPanel.add(stat4);
        statsPanel.add(Box.createHorizontalStrut(10));
        
        return statsPanel;
    }
    
    static JPanel createEnhancedStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background
                g2d.setColor(new Color(35, 50, 75));
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                
                // Border with accent color
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        
        card.setLayout(new GridLayout(2, 1, 0, 8));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(220, 110));
        card.setMaximumSize(new Dimension(220, 110));
        card.setMinimumSize(new Dimension(220, 110));
        
        // Title Label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(150, 180, 220));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        
        // Value Label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setVerticalAlignment(JLabel.CENTER);
        
        card.add(titleLabel);
        card.add(valueLabel);
        
        // Hover effect
        java.awt.event.MouseAdapter hoverAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(45, 65, 95));
                card.repaint();
                titleLabel.setForeground(accentColor.brighter());
                valueLabel.setForeground(accentColor.brighter());
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(35, 50, 75));
                card.repaint();
                titleLabel.setForeground(new Color(150, 180, 220));
                valueLabel.setForeground(TEXT_PRIMARY);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        
        card.addMouseListener(hoverAdapter);
        titleLabel.addMouseListener(hoverAdapter);
        valueLabel.addMouseListener(hoverAdapter);
        
        return card;
    }
    
    static JButton createInteractiveButton(String text) {
        return createInteractiveButton(text, ACCENT_PURPLE);
    }
    
    static JButton createInteractiveButton(String text, Color borderColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(borderColor.darker());
                } else if (getModel().isArmed()) {
                    g2d.setColor(borderColor.brighter());
                } else {
                    g2d.setColor(CARD_BG);
                }
                
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(CARD_BG);
        btn.setForeground(TEXT_PRIMARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override 
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(borderColor.brighter());
            }
            @Override 
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(TEXT_PRIMARY);
            }
        });
        
        return btn;
    }
    
    static void showStatsDashboard() {
        if (studentCount == 0) { showMessage("No data available for statistics"); return; }
        
        JFrame dashFrame = new JFrame("[STATS] Analytics Dashboard");
        dashFrame.setSize(700, 600);
        dashFrame.getContentPane().setBackground(DARK_BG);
        dashFrame.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setPreferredSize(new Dimension(100, 50));
        JLabel headerLabel = new JLabel("System Analytics Dashboard");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerPanel.add(headerLabel);
        dashFrame.add(headerPanel, BorderLayout.NORTH);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        statsPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        statsPanel.setBackground(DARK_BG);

        float avgCgpa = 0;
        for (int i = 0; i < studentCount; i++) avgCgpa += students[i].cgpa;
        avgCgpa /= studentCount;

        addStatRow(statsPanel, "Total Students", String.valueOf(studentCount), new Color(52, 152, 219));
        addStatRow(statsPanel, "Average CGPA", String.format("%.2f", avgCgpa), new Color(155, 89, 182));
        addStatRow(statsPanel, "Exam Queue Size", String.valueOf(rear - front), new Color(46, 204, 113));
        addStatRow(statsPanel, "Recent Additions", String.valueOf(top + 1), new Color(241, 196, 15));
        addStatRow(statsPanel, "Highest CGPA", String.format("%.2f", students[0].cgpa), new Color(230, 126, 34));
        
        float minCgpa = students[0].cgpa;
        for (int i = 1; i < studentCount; i++) minCgpa = Math.min(minCgpa, students[i].cgpa);
        addStatRow(statsPanel, "Lowest CGPA", String.format("%.2f", minCgpa), new Color(236, 112, 99));

        dashFrame.add(statsPanel, BorderLayout.CENTER);
        dashFrame.setLocationRelativeTo(null);
        dashFrame.setVisible(true);
    }
    
    static void addStatRow(JPanel panel, String title, String value, Color color) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(30, 40, 60));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(TEXT_PRIMARY);

        row.add(titleLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        panel.add(row);
    }
    
    static void exportData() {
        if (studentCount == 0) { showMessage("No data to export"); return; }
        
        StringBuilder data = new StringBuilder();
        data.append("STUDENT DATABASE EXPORT\n");
        data.append("========================\n");
        data.append("Exported on: ").append(new java.util.Date()).append("\n\n");
        data.append("TOTAL STUDENTS: ").append(studentCount).append("\n\n");

        for (int i = 0; i < studentCount; i++) {
            data.append(String.format("Student %d: %s | Roll: %d | Reg: %d | Course: %s | CGPA: %.2f\n",
                    i+1, students[i].name, students[i].rollNo, students[i].registrationNumber,
                    students[i].course, students[i].cgpa));
        }

        JTextArea exportArea = new JTextArea(data.toString());
        styleResultArea(exportArea);
        showResultFrame("[EXPORT] Database Backup", exportArea);
    }

    static void showAddForm() {
        JFrame addFrame = new JFrame("[ADD] New Student Record");
        addFrame.setSize(550, 650);
        addFrame.setLayout(new BorderLayout());
        addFrame.getContentPane().setBackground(DARK_BG);
        addFrame.getRootPane().setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 2));

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_BLUE, 
                                                          getWidth(), 0, ACCENT_PURPLE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(100, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        JLabel headerLabel = new JLabel("Add Student to System");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerPanel.add(headerLabel);
        addFrame.add(headerPanel, BorderLayout.NORTH);

        // ========== FORM PANEL ==========
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 5, 20));
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(DARK_BG);

        JTextField nameField = createAdvancedTextField("Enter full name");
        JTextField rollField = createAdvancedTextField("Enter roll number");
        JTextField regField = createAdvancedTextField("Enter registration number");
        JTextField cgpaField = createAdvancedTextField("Enter CGPA (0-10)");

        String[] courseOptions = {"Select Course", "B.Tech", "M.Tech", "BCA", "MCA", "BBA", "MBA", "B.Sc", "M.Sc"};
        JComboBox<String> courseDropdown = new JComboBox<>(courseOptions);
        courseDropdown.setForeground(TEXT_PRIMARY);
        courseDropdown.setBackground(CARD_BG);
        courseDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        courseDropdown.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, ACCENT_PURPLE),
                new EmptyBorder(10, 10, 10, 10)));

        // Form fields with labels
        addFormField(formPanel, "Full Name", nameField);
        addFormField(formPanel, "Roll Number", rollField);
        addFormField(formPanel, "Registration Number", regField);
        addFormField(formPanel, "Course", courseDropdown);
        addFormField(formPanel, "CGPA", cgpaField);

        addFrame.add(formPanel, BorderLayout.CENTER);

        // ========== BUTTON PANEL ==========
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setBorder(new EmptyBorder(0, 40, 25, 40));
        btnPanel.setBackground(DARK_BG);

        JButton submit = createInteractiveFormButton("SUBMIT", new Color(46, 204, 113));
        JButton cancel = createInteractiveFormButton("CANCEL", new Color(231, 76, 60));
        
        cancel.addActionListener(e -> addFrame.dispose());

        submit.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int roll = Integer.parseInt(rollField.getText().trim());
                int reg = Integer.parseInt(regField.getText().trim());
                float cgpa = Float.parseFloat(cgpaField.getText().trim());
                String course = (String) courseDropdown.getSelectedItem();

                if (name.isEmpty()) { showMessage("ERROR: Name cannot be empty!"); return; }
                if ("Select Course".equals(course)) { showMessage("ERROR: Please select a course!"); return; }
                if (cgpa < 0 || cgpa > 10) { showMessage("ERROR: CGPA must be between 0-10"); return; }
                if (studentCount >= students.length) { showMessage("ERROR: Student list is full"); return; }

                Student s = new Student(name, roll, reg, course, cgpa);
                students[studentCount++] = s;
                pushToStack(s);
                showMessage("SUCCESS: Student added successfully!\n\nName: " + name + "\nReg#: " + reg);
                addFrame.dispose();
            } catch (NumberFormatException ex) {
                showMessage("ERROR: Invalid input - Check numbers");
            }
        });

        btnPanel.add(submit);
        btnPanel.add(cancel);
        addFrame.add(btnPanel, BorderLayout.SOUTH);
        addFrame.setLocationRelativeTo(null);
        addFrame.setVisible(true);
    }
    
    static void addFormField(JPanel panel, String labelText, Component field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(DARK_BG);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(174, 214, 241));
        label.setPreferredSize(new Dimension(130, 40));
        
        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        panel.add(row);
    }
    
    static JTextField createAdvancedTextField(String hint) {
        JTextField field = new JTextField();
        field.setText(hint);
        field.setForeground(new Color(120, 140, 160));
        field.setBackground(CARD_BG);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, ACCENT_PURPLE),
                new EmptyBorder(10, 10, 10, 10)));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(new Color(120, 140, 160));
                }
            }
        });
        
        return field;
    }
    
    static JButton createInteractiveFormButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isArmed()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        
        return btn;
    }

    static void showAllStudents() {
        if (studentCount == 0) { showMessage("No students added yet"); return; }
        JTextArea area = new JTextArea();
        styleResultArea(area);
        
        area.append("================================================================================\n");
        area.append(String.format("%-5s | %-15s | %-8s | %-15s | %-6s\n", "Roll", "Name", "Reg#", "Course", "CGPA"));
        area.append("================================================================================\n\n");
        
        for (int i = 0; i < studentCount; i++) {
            area.append(String.format("%-5d | %-15s | %-8d | %-15s | %.2f\n", 
                    students[i].rollNo, students[i].name, students[i].registrationNumber, 
                    students[i].course, students[i].cgpa));
            area.append("--------------------------------------------------------------------------------\n");
        }
        showResultFrame("[ALL] All Students (" + studentCount + " Total)", area);
    }

    static void searchByRegNumber() {
        String input = JOptionPane.showInputDialog(null, "Enter Registration Number:", "Search Student", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            int reg = Integer.parseInt(input);
            for (int i = 0; i < studentCount; i++) {
                if (students[i].registrationNumber == reg) {
                    JTextArea area = new JTextArea();
                    styleResultArea(area);
                    area.append("=============================================\n");
                    area.append("          STUDENT FOUND\n");
                    area.append("=============================================\n\n");
                    area.append(String.format("Name         : %s\n", students[i].name));
                    area.append(String.format("Roll No      : %d\n", students[i].rollNo));
                    area.append(String.format("Reg No       : %d\n", students[i].registrationNumber));
                    area.append(String.format("Course       : %s\n", students[i].course));
                    area.append(String.format("CGPA         : %.2f\n", students[i].cgpa));
                    showResultFrame("[SEARCH] Search Result", area);
                    return;
                }
            }
            showMessage("ERROR: Student not found");
        } catch (NumberFormatException e) {
            showMessage("ERROR: Invalid Registration Number");
        }
    }

    static void deleteByRegNumber() {
        String input = JOptionPane.showInputDialog(null, "Enter Registration Number to Delete:", "Delete Student", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            int reg = Integer.parseInt(input);
            for (int i = 0; i < studentCount; i++) {
                if (students[i].registrationNumber == reg) {
                    String name = students[i].name;
                    System.arraycopy(students, i + 1, students, i, studentCount - i - 1);
                    students[--studentCount] = null;
                    showMessage("SUCCESS: Student '" + name + "' deleted successfully!");
                    return;
                }
            }
            showMessage("ERROR: Student not found");
        } catch (NumberFormatException e) {
            showMessage("ERROR: Invalid Registration Number");
        }
    }

    static void sortByCGPA() {
        for (int i = 0; i < studentCount - 1; i++) {
            for (int j = 0; j < studentCount - i - 1; j++) {
                if (students[j].cgpa < students[j + 1].cgpa) {
                    Student tmp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = tmp;
                }
            }
        }
        showMessage("SUCCESS: Students sorted by CGPA (High to Low)");
    }

    static void showTop3Students() {
        if (studentCount == 0) { showMessage("No students added"); return; }
        sortByCGPA();
        JTextArea area = new JTextArea();
        styleResultArea(area);
        
        area.append("================================================================================\n");
        area.append("                    TOP 3 STUDENTS\n");
        area.append("================================================================================\n\n");
        
        for (int i = 0; i < Math.min(3, studentCount); i++) {
            String medal = (i == 0) ? "[GOLD]" : (i == 1) ? "[SILVER]" : "[BRONZE]";
            area.append(String.format("%s %s\n", medal, (i + 1) + ". " + students[i].name + 
                    " (CGPA: " + students[i].cgpa + ")"));
            area.append("--------------------------------------------------------------------------------\n");
        }
        showResultFrame("[TOP] Top 3 Students", area);
    }

    static void showRecentAdditions() {
        if (top == -1) { showMessage("Stack is empty"); return; }
        JTextArea area = new JTextArea();
        styleResultArea(area);
        
        area.append("================================================================================\n");
        area.append("                    RECENT ADDITIONS\n");
        area.append("================================================================================\n\n");
        
        for (int i = top; i >= 0; i--) {
            area.append((top - i) + ". " + recentStack[i].name + " - " + recentStack[i].course + "\n");
            area.append("--------------------------------------------------------------------------------\n");
        }
        showResultFrame("[RECENT] Recent Additions", area);
    }

    static void examRegistration() {
        String input = JOptionPane.showInputDialog(null, "Enter Registration Number for Exam:", "Exam Registration", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            int reg = Integer.parseInt(input);
            for (int i = 0; i < studentCount; i++) {
                if (students[i].registrationNumber == reg) {
                    if (rear >= examQueue.length) { showMessage("Exam queue is full"); return; }
                    examQueue[rear++] = students[i];
                    showMessage("SUCCESS: " + students[i].name + " registered for exam!");
                    return;
                }
            }
            showMessage("ERROR: Student not found");
        } catch (NumberFormatException e) {
            showMessage("ERROR: Invalid Registration Number");
        }
    }

    static void showExamQueue() {
        if (front == rear) { showMessage("Exam queue is empty"); return; }
        JTextArea area = new JTextArea();
        styleResultArea(area);
        
        area.append("================================================================================\n");
        area.append("                   EXAM REGISTRATIONS\n");
        area.append("================================================================================\n\n");
        
        for (int i = front; i < rear; i++) {
            area.append((i + 1 - front) + ". " + examQueue[i].name + " - Reg#: " + 
                    examQueue[i].registrationNumber + "\n");
            area.append("--------------------------------------------------------------------------------\n");
        }
        showResultFrame("[QUEUE] Exam Queue (" + (rear - front) + " Students)", area);
    }

    static void showBarGraph() {
        if (studentCount == 0) { showMessage("No students to graph"); return; }
        JFrame graphFrame = new JFrame("[GRAPH] CGPA Bar Chart");
        graphFrame.setSize(1000, 500);
        graphFrame.getContentPane().setBackground(DARK_BG);
        graphFrame.add(new BarGraphPanel());
        graphFrame.setLocationRelativeTo(null);
        graphFrame.setVisible(true);
    }

    static class BarGraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(DARK_BG);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw gradient background
            GradientPaint bgGradient = new GradientPaint(0, 0, DARK_BG, 0, getHeight(), new Color(40, 60, 100));
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Draw title
            g2d.setColor(TEXT_PRIMARY);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
            g2d.drawString("Student CGPA Distribution Chart", 50, 50);

            // Axes
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(60, 400, 900, 400);  // X-axis
            g2d.drawLine(60, 400, 60, 80);   // Y-axis

            // Y-axis labels
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            for (int i = 0; i <= 10; i++) {
                int y = 400 - i * 32;
                g2d.setColor(new Color(100, 100, 150));
                g2d.drawLine(55, y, 60, y);
                g2d.setColor(TEXT_PRIMARY);
                g2d.drawString(String.valueOf(i), 30, y + 5);
            }

            // Draw bars
            int barWidth = 50;
            int spacing = 15;
            int x = 80;
            Color[] colors = {new Color(52, 152, 219), new Color(155, 89, 182), new Color(46, 204, 113), 
                             new Color(241, 196, 15), new Color(230, 126, 34)};

            for (int i = 0; i < studentCount && x < 900; i++) {
                int height = (int) (students[i].cgpa * 32);
                Color barColor = colors[i % colors.length];
                
                // Draw bar with gradient
                GradientPaint barGradient = new GradientPaint(x, 400 - height, barColor.brighter(), 
                                                             x, 400, barColor.darker());
                g2d.setPaint(barGradient);
                g2d.fillRect(x, 400 - height, barWidth, height);
                
                // Draw border
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(x, 400 - height, barWidth, height);

                // Draw name
                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String name = students[i].name.length() > 8 ? 
                            students[i].name.substring(0, 8) : students[i].name;
                g2d.drawString(name, x - 5, 420);

                // Draw CGPA value on top of bar
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2d.drawString(String.format("%.1f", students[i].cgpa), x + 5, 400 - height - 5);

                x += barWidth + spacing;
            }

            // Draw legend
            g2d.setColor(TEXT_PRIMARY);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2d.drawString("CGPA Range: 0 - 10", 60, 460);
        }
    }

    static void pushToStack(Student s) {
        if (top < recentStack.length - 1) {
            recentStack[++top] = s;
        }
    }

    static void styleResultArea(JTextArea area) {
        area.setBackground(CARD_BG);
        area.setForeground(TEXT_PRIMARY);
        area.setFont(new Font("Courier New", Font.PLAIN, 14));
        area.setEditable(false);
        area.setBorder(new EmptyBorder(15, 15, 15, 15));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    static JTextArea createStyledResultArea(String text) {
        JTextArea area = new JTextArea(text);
        styleResultArea(area);
        return area;
    }

    static void showResultFrame(String title, JTextArea area) {
        JFrame resultFrame = new JFrame(title);
        resultFrame.setSize(700, 450);
        resultFrame.getContentPane().setBackground(DARK_BG);
        resultFrame.getRootPane().setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 2));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setPreferredSize(new Dimension(100, 40));
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerPanel.add(headerLabel);
        resultFrame.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 1));
        scroll.getViewport().setBackground(CARD_BG);
        resultFrame.add(scroll, BorderLayout.CENTER);
        
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
    }

    static void showMessage(String msg) {
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        JOptionPane.showMessageDialog(null, msg);
    }
}


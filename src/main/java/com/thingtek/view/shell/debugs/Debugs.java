package com.thingtek.view.shell.debugs;

import com.thingtek.view.shell.BasePanel;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Debugs extends BasePanel {

    private JTextArea jta = null;
    private String lineFeed = "\r\n";
    private boolean isShow = false;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JPopupMenu pop;
    private JMenuItem copy = null, cut = null, delete = null, clear = null;


    public Debugs init() {
        isShow = true;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        pop = new JPopupMenu();
        copy = new JMenuItem("复制");
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jta.copy();
            }
        });
        pop.add(copy);

        cut = new JMenuItem("剪切");
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutText();
            }
        });
        pop.add(cut);

        delete = new JMenuItem("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteText();
            }
        });
        pop.add(delete);

        clear = new JMenuItem("清空");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jta.setText("");
                jta.repaint();
            }
        });
        pop.add(clear);

        jta = new JTextArea();
        jta.setLineWrap(true);// 自动换行
        jta.setWrapStyleWord(true);// 断行不断字
        jta.setFont(factorys.getFontFactory().getFont("font14"));

        jta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                int buttonKey = arg0.getButton();
//                // 双击清屏
//                if (buttonKey == MouseEvent.BUTTON1 && arg0.getClickCount() == 2) {
//                    jta.setText(null);
//                }
                if (buttonKey == MouseEvent.BUTTON3) {
                    boolean visible = isSeleted();
                    copy.setEnabled(visible);
                    cut.setEnabled(visible);
                    delete.setEnabled(visible);
                    visible = !(jta.getText().equals(""));
                    clear.setEnabled(visible);
                    pop.show(jta, arg0.getX(), arg0.getY());
                }
            }
        });
        jta.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (isSeleted()) {
                    super.insertString(offs, str, a);
                    if (jta.getLineCount() >= 10000) {
                        jta.setText("");
                    }
                    return;
                }
                try {
                    int off = jta.getLineEndOffset(jta.getLineCount() - 1000);
                    remove(0, off);
                    super.insertString(offs - off, str, a);
                } catch (BadLocationException e) {
                    // e.printStackTrace();
                    super.insertString(offs, str, a);
                } finally {
                    repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(jta);
        this.add(scrollPane, BorderLayout.CENTER);

        return this;
    }

    public synchronized String rec(byte[] data, int len, Date time, String msg) {
        String rece = timeFormat.format(time) + " " + msg
                + " →→收 : " + getBufHexStr(data, 0, len) + lineFeed;
        if (isShow) {
            jta.append(rece);
            if (jta.getLineCount() > 1000) {
                subString();
            }
        }
        return rece;
    }

    public synchronized String send(byte[] data, int len, String dtu) {
        String send = timeFormat.format(Calendar.getInstance().getTime()) + " " + dtu
                + " 发→→ : " + getBufHexStr(data, 0, len) + lineFeed;
        if (isShow) {
            jta.append(send);
//            jta.append(String.valueOf(System.currentTimeMillis())+lineFeed);
            if (jta.getLineCount() > 1000) {
                subString();
            }
        }
        return send;
    }

    private synchronized void subString() {
        try {
            if (isSeleted()) {
                if (jta.getLineCount() > 5000) {
                    jta.setText("");
                }
                return;
            }
            int off = jta.getLineEndOffset(jta.getLineCount() - 1000);
            jta.replaceRange("", 0, off);
            jta.repaint();
        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            notify();
        }
    }


    public synchronized void showMsg(String s) {
        if (!isShow) {
            return;
        }
        jta.append(timeFormat.format(Calendar.getInstance().getTime()) + s
                + lineFeed);
        if (jta.getLineCount() > 1000) {
            subString();
        }
    }

    // 光标是否选中
    private boolean isSeleted() {
        boolean b = false;
        if (jta.getSelectedText() != null) {
            b = true;
        }
        return b;
    }

    // 剪切
    private void cutText() {
        if (isSeleted()) {
            jta.copy();
            jta.replaceRange("", jta.getSelectionStart(), jta.getSelectionEnd());
        }
    }

    // 删除
    private void deleteText() {
        if (isSeleted()) {
            jta.replaceRange("", jta.getSelectionStart(), jta.getSelectionEnd());
        }
    }

    private String getBufHexStr(byte[] b, int off, int len) {
        if (b == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * len);
        for (int i = off; i < off + len; i++) {
            String HEXES = "0123456789ABCDEF";
            hex.append(HEXES.charAt((b[i] & 0xF0) >> 4)).append(HEXES.charAt((b[i] & 0x0F))).append(" ");
        }
        return hex.toString();
    }

}

package org.teamrocket;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractApplicationAction;
import org.jhotdraw.gui.URIChooser;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.util.prefs.PreferencesUtil;

public class SimulateXMLFileAction extends AbstractApplicationAction {

  public final static String ID = "file.simulatexml";

  /** Creates a new instance. */
  public SimulateXMLFileAction(Application app) {
    super(app);
    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
    labels.configureAction(this, ID);
  }

  protected URIChooser getChooser(View view) {
    // Note: We pass null here, because we want the application-wide chooser
    return getApplication().getOpenChooser(null);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    final Application app = getApplication();
    if (app.isEnabled()) {
      app.setEnabled(false);
      // Search for an empty view
      View emptyView = app.getActiveView();
      if (emptyView == null || !emptyView.isEmpty() || !emptyView.isEnabled()) {
        emptyView = null;
      }

      final View view;
      boolean disposeView;
      if (emptyView == null) {
        view = app.createView();
        app.add(view);
        disposeView = true;
      }
      else {
        view = emptyView;
        disposeView = false;
      }
      URIChooser chooser = getChooser(view);
      chooser.setDialogType(JFileChooser.OPEN_DIALOG);
      if (showDialog(chooser, app.getComponent()) == JFileChooser.APPROVE_OPTION) {
        app.show(view);
        openViewFromURI(view, chooser.getSelectedURI(), chooser);
      }
      else {
        if (disposeView) {
          app.dispose(view);
        }
        app.setEnabled(true);
      }
    }
  }

  protected void openViewFromURI(final View view, final URI uri, final URIChooser chooser) {
    final Application app = getApplication();
    app.setEnabled(true);
    view.setEnabled(false);

    // If there is another view with the same URI we set the multiple open
    // id of our view to max(multiple open id) + 1.
    int multipleOpenId = 1;
    for (View aView : app.views()) {
      if (aView != view && aView.isEmpty()) {
        multipleOpenId = Math.max(multipleOpenId, aView.getMultipleOpenId() + 1);
      }
    }
    view.setMultipleOpenId(multipleOpenId);
    view.setEnabled(false);

    // TODO Open the file

    view.setURI(uri);
    view.setEnabled(true);
    app.setEnabled(true);
    view.getComponent().requestFocus();
    app.addRecentURI(uri);

    (new Thread(new Simulate())).start();
  }

  /**
   * We implement JFileChooser.showDialog by ourselves, so that we can center dialogs properly on screen on Mac OS X.
   */
  public int showDialog(URIChooser chooser, Component parent) {
    final Component finalParent = parent;
    final int[] returnValue = new int[1];
    final JDialog dialog = createDialog(chooser, finalParent);
    dialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        returnValue[0] = JFileChooser.CANCEL_OPTION;
      }
    });
    chooser.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CancelSelection")) {
          returnValue[0] = JFileChooser.CANCEL_OPTION;
          dialog.setVisible(false);
        }
        else if (e.getActionCommand().equals("ApproveSelection")) {
          returnValue[0] = JFileChooser.APPROVE_OPTION;
          dialog.setVisible(false);
        }
      }
    });
    returnValue[0] = JFileChooser.ERROR_OPTION;
    chooser.rescanCurrentDirectory();

    dialog.setVisible(true);
    // chooser.firePropertyChange("JFileChooserDialogIsClosingProperty", dialog,
    // null);
    dialog.removeAll();
    dialog.dispose();
    return returnValue[0];
  }

  /**
   * We implement JFileChooser.showDialog by ourselves, so that we can center dialogs properly on screen on Mac OS X.
   */
  protected JDialog createDialog(URIChooser chooser, Component parent) throws HeadlessException {
    String title = chooser.getDialogTitle();
    if (chooser instanceof JFileChooser) {
      ((JFileChooser) chooser).getAccessibleContext().setAccessibleDescription(title);
    }

    JDialog dialog;
    Window window = (parent == null || (parent instanceof Window)) ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
    if (window instanceof Frame) {
      dialog = new JDialog((Frame) window, title, true);
    }
    else {
      dialog = new JDialog((Dialog) window, title, true);
    }
    dialog.setComponentOrientation(chooser.getComponent().getComponentOrientation());

    Container contentPane = dialog.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(chooser.getComponent(), BorderLayout.CENTER);

    if (JDialog.isDefaultLookAndFeelDecorated()) {
      boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
      if (supportsWindowDecorations) {
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
      }
    }
    // dialog.pack();
    Preferences prefs = PreferencesUtil.userNodeForPackage(getApplication().getModel().getClass());

    PreferencesUtil.installFramePrefsHandler(prefs, "openChooser", dialog);
    /*
     * if (window.getBounds().isEmpty()) { Rectangle screenBounds = window.getGraphicsConfiguration().getBounds(); dialog.setLocation(screenBounds.x +
     * (screenBounds.width - dialog.getWidth()) / 2, // screenBounds.y + (screenBounds.height - dialog.getHeight()) / 3); } else {
     * dialog.setLocationRelativeTo(parent); }
     */

    return dialog;
  }
}
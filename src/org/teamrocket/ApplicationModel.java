package org.teamrocket;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.DefaultApplicationModel;
import org.jhotdraw.app.DefaultMenuBuilder;
import org.jhotdraw.app.MenuBuilder;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.ActionUtil;
import org.jhotdraw.app.action.file.ExportFileAction;
import org.jhotdraw.app.action.file.OpenDirectoryAction;
import org.jhotdraw.app.action.file.OpenFileAction;
import org.jhotdraw.app.action.view.ToggleViewPropertyAction;
import org.jhotdraw.app.action.view.ViewPropertyAction;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.action.ButtonFactory;
import org.jhotdraw.draw.tool.ConnectionTool;
import org.jhotdraw.draw.tool.CreationTool;
import org.jhotdraw.draw.tool.TextAreaCreationTool;
import org.jhotdraw.draw.tool.Tool;
import org.jhotdraw.gui.JFileURIChooser;
import org.jhotdraw.gui.URIChooser;
import org.jhotdraw.gui.filechooser.ExtensionFileFilter;
import org.jhotdraw.samples.pert.PertView;
import org.jhotdraw.samples.pert.figures.DependencyFigure;
import org.jhotdraw.util.ResourceBundleUtil;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ApplicationModel extends DefaultApplicationModel {

  private final static double[] scaleFactors = { 5, 4, 3, 2, 1.5, 1.25, 1,
      0.75, 0.5, 0.25, 0.10 };

  private static class ToolButtonListener implements ItemListener {

    private Tool tool;
    private DrawingEditor editor;

    public ToolButtonListener(Tool t, DrawingEditor editor) {
      this.tool = t;
      this.editor = editor;
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
      if (evt.getStateChange() == ItemEvent.SELECTED) {
        editor.setTool(tool);
      }
    }
  }

  /**
   * This editor is shared by all views.
   */
  private DefaultDrawingEditor sharedEditor;
  private HashMap<String, Action> actions;

  private static List<StateEntity> bucket = new ArrayList<StateEntity>();
  private static List<StateEntity> start = new ArrayList<StateEntity>();

  public static void addStateEntity(StateEntity s) {
    bucket.add(s);
  }

  public static List<StateEntity> getStateEntity() {
    return bucket;
  }

  public static void addStartStateEntity(StateEntity s) {
    // TODO : If list is not empty, warn
    if (!start.isEmpty()) {
      // warning
    }
    start.add(s);
  }

  public static StateEntity getStartEntity() {
    return start.get(0);
  }

  /** Creates a new instance. */
  public ApplicationModel() {
    // _start = new StateEntity(false);
  }

  @Override
  public ActionMap createActionMap(Application a, @Nullable View v) {
    ActionMap m = super.createActionMap(a, v);
    ResourceBundleUtil drawLabels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
    AbstractAction aa;

    m.put(ImportXMLFileAction.ID, new ImportXMLFileAction(a));
    m.put(ExportXMLFileAction.ID, new ExportXMLFileAction(a, v));
    m.put(ExportFileAction.ID, new ExportFileAction(a, v));
    m.put(SimulatorAction.ID, new SimulatorAction(a, v));

    m.put("view.toggleGrid", aa = new ToggleViewPropertyAction(a, v, PertView.GRID_VISIBLE_PROPERTY));
    drawLabels.configureAction(aa, "view.toggleGrid");
    for (double sf : scaleFactors) {
      m.put((int) (sf * 100) + "%", aa = new ViewPropertyAction(a, v, DrawingView.SCALE_FACTOR_PROPERTY, Double.TYPE, new Double(sf)));
      aa.putValue(Action.NAME, (int) (sf * 100) + " %");

    }
    return m;
  }

  public MenuBuilder getMenuBuilder() {
    return new DefaultMenuBuilder() {
      @Override
      public void addOpenFileItems(JMenu m, Application app, @Nullable View v) {
        ActionMap am = app.getActionMap(v);
        Action a;
        if (null != (a = am.get(OpenFileAction.ID))) {
          m.add(a);
        }
        if (null != (a = am.get(OpenDirectoryAction.ID))) {
          m.add(a);
        }
        if (null != (a = am.get(ImportXMLFileAction.ID))) {
          m.add(a);
        }
      }

      @Override
      public void addExportFileItems(JMenu m, Application app, @Nullable View v) {
        ActionMap am = app.getActionMap(v);
        Action a;
        if (null != (a = am.get(ExportFileAction.ID))) {
          m.add(a);
        }
        if (null != (a = am.get(ExportXMLFileAction.ID))) {
          m.add(a);
        }
      }

      @Override
      public void addOtherFileItems(JMenu m, Application app, @Nullable View v) {
        ActionMap am = app.getActionMap(v);
        Action a;
        if (null != (a = am.get(SimulatorAction.ID))) {
          m.add(a);
        }
      }
    };
  }

  public DefaultDrawingEditor getSharedEditor() {
    if (sharedEditor == null) {
      sharedEditor = new DefaultDrawingEditor();
    }
    return sharedEditor;
  }

  @Override
  public void initView(Application a, @Nullable View p) {
    if (a.isSharingToolsAmongViews()) {
      ((PertView) p).setEditor(getSharedEditor());
    }
  }

  private void addCreationButtonsTo(JToolBar tb, final DrawingEditor editor) {
    // AttributeKeys for the entitie sets

    HashMap<AttributeKey, Object> attributes;

    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");
    ResourceBundleUtil drawLabels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");

    ButtonFactory.addSelectionToolTo(tb, editor);
    tb.addSeparator();

    attributes = new HashMap<AttributeKey, Object>();
    attributes.put(AttributeKeys.FILL_COLOR, Color.white);
    attributes.put(AttributeKeys.STROKE_COLOR, Color.black);
    attributes.put(AttributeKeys.TEXT_COLOR, Color.black);
    ButtonFactory.addToolTo(tb, editor, new CreationTool(new StateFigure(), attributes), "edit.createState", labels);
    ButtonFactory.addToolTo(tb, editor, new CreationTool(new StartStateFigure(), attributes), "edit.createStartState", labels);
    ButtonFactory.addToolTo(tb, editor, new CreationTool(new EndStateFigure(), attributes), "edit.createEndState", labels);

    attributes = new HashMap<AttributeKey, Object>();
    attributes.put(AttributeKeys.STROKE_COLOR, new Color(0x000099));
    ButtonFactory.addToolTo(tb, editor, new ConnectionTool(new TransitionFigure(), attributes), "edit.createDependency", labels);
    tb.addSeparator();
    ButtonFactory.addToolTo(tb, editor, new TextAreaCreationTool(new TextAreaFigure()), "edit.createTextArea", drawLabels);
  }

  /**
   * Creates toolbars for the application. This class always returns an empty
   * list. Subclasses may return other values.
   */
  @Override
  public java.util.List<JToolBar> createToolBars(Application a,
      @Nullable View pr) {
    ResourceBundleUtil drawLabels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
    PertView p = (PertView) pr;

    DrawingEditor editor;
    if (p == null) {
      editor = getSharedEditor();
    }
    else {
      editor = p.getEditor();
    }

    LinkedList<JToolBar> list = new LinkedList<JToolBar>();
    JToolBar tb;
    tb = new JToolBar();
    addCreationButtonsTo(tb, editor);
    tb.setName(drawLabels.getString("window.drawToolBar.title"));
    list.add(tb);
    // tb = new JToolBar();
    // ButtonFactory.addAttributesButtonsTo(tb, editor);
    // tb.setName(drawLabels.getString("window.attributesToolBar.title"));
    // list.add(tb);
    // tb = new JToolBar();
    // ButtonFactory.addAlignmentButtonsTo(tb, editor);
    // tb.setName(drawLabels.getString("window.alignmentToolBar.title"));
    // list.add(tb);
    return list;
  }

  /** Creates the MenuBuilder. */
  @Override
  protected MenuBuilder createMenuBuilder() {
    return new DefaultMenuBuilder() {

      @Override
      public void addOtherViewItems(JMenu m, Application app, @Nullable View v) {
        ActionMap am = app.getActionMap(v);
        JCheckBoxMenuItem cbmi;
        cbmi = new JCheckBoxMenuItem(am.get("view.toggleGrid"));
        ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get("view.toggleGrid"));
        m.add(cbmi);
        JMenu m2 = new JMenu("Zoom");
        for (double sf : scaleFactors) {
          String id = (int) (sf * 100) + "%";
          cbmi = new JCheckBoxMenuItem(am.get(id));
          ActionUtil.configureJCheckBoxMenuItem(cbmi, am.get(id));
          m2.add(cbmi);
        }
        m.add(m2);
      }
    };
  }

  @Override
  public URIChooser createOpenChooser(Application a, @Nullable View v) {
    JFileURIChooser c = new JFileURIChooser();
    c.addChoosableFileFilter(new ExtensionFileFilter("UMLCreator Diagram", "umlc"));
    return c;
  }

  @Override
  public URIChooser createSaveChooser(Application a, @Nullable View v) {
    JFileURIChooser c = new JFileURIChooser();
    c.addChoosableFileFilter(new ExtensionFileFilter("UMLCreator Diagram", "umlc"));
    return c;
  }

  private StateEntity _start;
}
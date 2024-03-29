package org.teamrocket.figures;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.FONT_BOLD;
import static org.jhotdraw.draw.AttributeKeys.STROKE_COLOR;
import static org.jhotdraw.draw.AttributeKeys.STROKE_DASHES;
import static org.jhotdraw.draw.AttributeKeys.TEXT;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.GraphicalCompositeFigure;
import org.jhotdraw.draw.ListFigure;
import org.jhotdraw.draw.RoundRectangleFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.LocatorConnector;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.ConnectorHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.ResizeHandleKit;
import org.jhotdraw.draw.layouter.VerticalLayouter;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.geom.Insets2D;
import org.jhotdraw.samples.pert.figures.SeparatorLineFigure;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;
import org.jhotdraw.xml.DOMOutput;
import org.teamrocket.ApplicationModel;
import org.teamrocket.entities.ContextMenuItemAction;
import org.teamrocket.entities.ContextSubMenuItemAction;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

public class StateFigure extends GraphicalCompositeFigure {

  protected StateEntity _stateEntity;
  private TextFigure _nameFigure;
  private HashSet<Action> _actions;
  private HashSet<Action> _disassociate;
  public static int num_of_states = 0;

  public StateFigure() {
    super(new RoundRectangleFigure());
    _actions = new HashSet<Action>();
    _disassociate = new HashSet<Action>();
    init();
    // may want to change to false, depending on how we use the boolean value in
    // StateEntity.java
  }

  public StateFigure(Figure figure) {
    super(figure);
    init();
  }

  @Override
  public Collection<Handle> createHandles(int detailLevel) {
    java.util.List<Handle> handles = new LinkedList<Handle>();

    switch (detailLevel) {
    case -1:
      handles.add(new BoundsOutlineHandle(getPresentationFigure(), false, true));
      break;
    case 0:
      /*
       * handles.add(new MoveHandle(this, RelativeLocator.northWest())); handles.add(new MoveHandle(this, RelativeLocator.northEast())); handles.add(new
       * MoveHandle(this, RelativeLocator.southWest())); handles.add(new MoveHandle(this, RelativeLocator.southEast()));
       */

      ResizeHandleKit.addCornerResizeHandles(this, handles);

      ConnectorHandle ch;
      handles.add(ch = new ConnectorHandle(new LocatorConnector(this, RelativeLocator.center()), new TransitionFigure()));
      ch.setToolTipText("Drag the connector to a dependent state.");
      break;
    }
    return handles;
  }

  /* public void update(Observable obs, Object o) { // TODO: } */

  public void setName(String newValue) {
    willChange();
    _nameFigure.setText(newValue);
    changed();
  }

  public String getName() {
    return getNameFigure().getText();
  }

  public StateEntity getEntity() {
    return _stateEntity;
  }

  // TaskFigure has one of these - it might be for some unseen functionality of
  // another class
  @Override
  public int getLayer() {
    return 0;
  }

  // not sure how this works, but TaskFigure seems to like it
  private TextFigure getNameFigure() {
    return (TextFigure) ((ListFigure) getChild(0)).getChild(0);
  }

  @Override
  public Collection<Action> getActions(Point2D.Double p) {
    if (getEntity().getParent() == null)
      return _actions;

    return _disassociate;
  }

  // modifiers for predecessor and successor lists
  public void addPredecessor(TransitionEntity t) {
    _stateEntity.addPredecessor(t);

  }

  public void removePredecessor(TransitionEntity t) {
    _stateEntity.removePredecessor(t);
  }

  public void addSuccessor(TransitionEntity t) {
    _stateEntity.addSuccessor(t);
  }

  public void removeSuccessor(TransitionEntity t) {
    _stateEntity.removeSuccessor(t);
  }

  public void addBlankInternalTransition() {
    _internalTransitions.add(new InternalTransitionFigure());

    // TODO : Cause redraw
  }

  @Override
  public void read(DOMInput in) throws IOException {
    double x = in.getAttribute("x", 0d);
    double y = in.getAttribute("y", 0d);
    double w = in.getAttribute("w", 0d);
    double h = in.getAttribute("h", 0d);
    setBounds(new Point2D.Double(x, y), new Point2D.Double(x + w, y + h));
    readAttributes(in);
    in.openElement("model");
    in.openElement("name");
    setName((String) in.readObject());
    in.closeElement();
    in.openElement("duration");
    in.closeElement();
    in.closeElement();
  }

  @Override
  public void write(DOMOutput out) throws IOException {
    Rectangle2D.Double r = getBounds();
    out.addAttribute("x", r.x);
    out.addAttribute("y", r.y);
    writeAttributes(out);
    out.openElement("model");
    out.openElement("name");
    out.writeObject(getName());
    out.closeElement();
    out.openElement("duration");
    out.closeElement();
    out.closeElement();
  }

  // for resizing
  public boolean isTransformable() {
    return true;
  }

  @Override
  public StateFigure clone() {
    final StateFigure that = (StateFigure) super.clone();
    that.init();
    that.setName("State " + num_of_states++);

    that.willChange();

    that._actions = new HashSet<Action>();

    that._disassociate = new HashSet<Action>();
    that._disassociate.add(new ContextMenuItemAction(that));

    // TODO update when parent updates. Perhaps move to separate method.
    // if (_data.getParent() != null) {
    // that._actions.add(new ContextMenuItemAction("Unset Parent",
    // "Disassociate this child from it's parent.") {
    // });
    // }
    for (StateEntity s : ApplicationModel.getStateEntityBucket()) {
      if (s.getStateFigure() instanceof StartStateFigure || s.getStateFigure() instanceof EndStateFigure)
        continue;

      that.addStateToChildMenu(s);
      s.getStateFigure().addStateToChildMenu(that._stateEntity);
    }

    that.changed();
    ApplicationModel.addStateEntity(that._stateEntity);

    return that;
  }

  private void addStateToChildMenu(StateEntity ent) {
    _actions.add(new ContextSubMenuItemAction(this, "Add child state", ent));
  }

  protected StateFigure superDuperClone() {
    return (StateFigure) super.clone();
  }

  @Override
  public void removeNotify(Drawing drawing) {
    super.removeNotify(drawing);
    _stateEntity.setParent(null);
    ApplicationModel.removeState(this._stateEntity);
  }

  Color _oldColor;

  public void toggleHighlight() {
    willChange();
    // int xorColor = _figure.get(AttributeKeys.FILL_COLOR).getRGB() ^ Color.white.getRGB();
    if (_oldColor == null) {
      _oldColor = get(AttributeKeys.FILL_COLOR);
      set(AttributeKeys.FILL_COLOR, Color.green);
    }
    else {
      set(AttributeKeys.FILL_COLOR, _oldColor);
      _oldColor = null;
    }
    changed();
  }

  private class InternalTransitionFigure extends TextFigure {
    public InternalTransitionFigure() {
      set(FONT_BOLD, false);
      setText("<Event> # <Action>");
      setAttributeEnabled(FONT_BOLD, false);
    }

    public void setText(String newText) {
      String oldText = getText();
      super.setText(newText);
      StateEntity ent = StateFigure.this.getEntity();

      if (oldText != null && !oldText.equals("Text")) {
        String[] newPieces = newText.split("#");

        if (newPieces.length != 2) {
          JOptionPane.showConfirmDialog(null, "Input not of format \"<Event> # <Action>\"");
          return;
        }

        if (oldText.equals("<Event> # <Action>")) {
          ent.addInternalTransition(newPieces[0].trim(), newPieces[1].trim());
          StateFigure.this.addBlankInternalTransition();
        }
        else {
          String[] oldPieces = oldText.split("#");
          List<String> oldVals = ent.getInternalTransitions(oldPieces[0].trim());
          ent.removeInternalTransition(oldPieces[0].trim());

          for (String s : oldVals) {
            if (s.equals(oldPieces[1].trim()))
              ent.addInternalTransition(newPieces[0].trim(), newPieces[1].trim());
            else
              ent.addInternalTransition(oldPieces[0].trim(), s);
          }
        }
      }
    }
  }

  public void init() {
    final StateFigure self = this;
    this.removeAllChildren();
    _stateEntity = new StateEntity(self);
    _nameFigure = new TextFigure() {
      @Override
      public void setText(String newText) {
        self.willChange();

        _stateEntity.setLabel(newText);

        set(TEXT, newText);
        self.changed();
      }
    };

    _internalTransitions = new ListFigure();

    setLayouter(new VerticalLayouter());

    RoundRectangleFigure nameCompartmentPF = new RoundRectangleFigure();
    nameCompartmentPF.set(STROKE_COLOR, null);
    nameCompartmentPF.setAttributeEnabled(STROKE_COLOR, false);
    nameCompartmentPF.set(FILL_COLOR, null);
    nameCompartmentPF.setAttributeEnabled(FILL_COLOR, false);
    ListFigure nameCompartment = new ListFigure(nameCompartmentPF);
    SeparatorLineFigure separator1 = new SeparatorLineFigure();

    add(nameCompartment);
    add(separator1);
    add(_internalTransitions);

    Insets2D.Double insets = new Insets2D.Double(8, 16, 8, 16);
    nameCompartment.set(LAYOUT_INSETS, insets);
    _internalTransitions.set(LAYOUT_INSETS, insets);

    nameCompartment.add(_nameFigure);
    _nameFigure.set(FONT_BOLD, true);
    _nameFigure.setAttributeEnabled(FONT_BOLD, true);

    addBlankInternalTransition();

    setAttributeEnabled(STROKE_DASHES, false);

    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");

    setName(labels.getString("teamrocket.state.defaultName"));

    _stateEntity.setLabel(_nameFigure.getText());

  }

  private ListFigure _internalTransitions;
}
package fun.connor.lighter.compiler.error;

import fun.connor.lighter.compiler.model.Route;

import javax.lang.model.element.Element;

public class RouteError extends AbstractCompilerError {

    private Element primaryTarget;
    private Element secondaryTarget;

    private Route primaryRoute;
    private Route secondaryRoute;

    public RouteError(Element first, Element second, Route firstRoute, Route secondRoute) { //TODO: make AbstractCompilerError better
        if (first.toString().compareTo(second.toString()) > 0) { //for equality purposed
            this.primaryTarget = first;
            this.secondaryTarget = second;
            this.primaryRoute = firstRoute;
            this.secondaryRoute = secondRoute;
        } else {
            this.secondaryTarget = first;
            this.primaryTarget = second;
            this.secondaryRoute = firstRoute;
            this.primaryRoute = secondRoute;
        }
    }

    @Override
    public String toString() {
        return "When parsing elements " + getDetailedTargetName() + " and "
                + getDetailedElementName(secondaryTarget) + ":\n"
                + getDetail();
    }

    @Override
    public Element getTarget() {
        return primaryTarget;
    }

    @Override
    public String getDetail() {
        return "The routes \"" + primaryRoute.getTemplateStr() + "\" and \"" + secondaryRoute.getTemplateStr()
                + "\" are the same";
    }
}

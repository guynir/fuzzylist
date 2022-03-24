import {useLocation, useNavigate, useParams} from "react-router-dom";
import React from "react";

/**
 * Wraps a given component with routing information and control, such as location (access your URL), navigation access
 * and optional routeing parameters.
 *
 * @param Component React component to inject information and control into.
 * @return Factory method that creates a wrapper with embedded React component.
 */
export function withRouter(Component: typeof React.Component): (props: any) => JSX.Element {
    // Create a factory function that creates a new wrapper with the original "Component" embedded inside it.
    function WithRouterFactory(props: any): JSX.Element {
        let location = useLocation();
        let navigate = useNavigate();
        let params = useParams();
        return (
            <Component
                {...props}
                location={location}
                navigate={navigate}
                params={params}
            />
        );
    }

    return WithRouterFactory;
}

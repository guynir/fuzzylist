import React from "react";
import "./InputBoxWithButton.css"
import {Input} from "@mui/material";
import {InputProps} from "@mui/material/Input/Input";

export interface InputBoxWithButtonProps extends InputProps {
    onSubmit?: () => void
    errorMessage?: string
}

export default class InputBoxWithButton extends React.Component<InputBoxWithButtonProps, any> {

    private readonly inputProps: InputProps

    constructor(props: InputBoxWithButtonProps) {
        super(props)

        // We need to extract Input element properties.
        const {onSubmit, errorMessage, ...inputBoxProps} = this.props
        this.inputProps = inputBoxProps
    }

    render(): JSX.Element {
        return (
            <div className="input-box-with-button-wrapper">
                <div className="input-box-and-icon-wrapper">
                    <div className="input-wrapper">
                        <Input sx={{width: "100%"}} {...this.inputProps} onKeyUp={event => {
                            if (event.key === 'Enter') {
                                console.log(this.props.inputRef)
                                if (this.props.onSubmit) {
                                    this.props.onSubmit()
                                } else {
                                    if (this.props.onKeyUp) {
                                        this.props.onKeyUp(event)
                                    }
                                }
                            }
                        }}
                        />
                    </div>
                    <div className="icon-wrapper">
                        <img src="/icons8-add-100.png"
                             alt=""
                             onClick={() => {
                                 if (this.props.onSubmit) {
                                     this.props.onSubmit()
                                 }
                             }}
                        />
                    </div>
                </div>
                <div className="error-message-wrapper">
                    {this.props.errorMessage}
                </div>
            </div>
        )
    }
}
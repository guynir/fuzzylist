import {FormControlLabel, RadioGroup} from "@mui/material";
import Radio from '@mui/material/Radio';

import React, {RefObject} from "react";
import "./WelcomePage.css"
import {createNewList} from "../services/ListManagement";
import InputBoxWithButton from "../components/InputBoxWithButton";

interface WelcomePageState {
    errorMessage?: string
    leftToRight: boolean
}

export class WelcomePage extends React.Component<any, WelcomePageState> {

    private readonly inputBoxRef: RefObject<any>;

    constructor(props: any) {
        super(props);
        this.inputBoxRef = React.createRef();
        this.state = {
            errorMessage: undefined,
            leftToRight: true
        }
    }

    handleChange(leftToRight: boolean) {
        this.setState({
            leftToRight: leftToRight,
            errorMessage: this.state.errorMessage
        })
    }
    render() {
        return (
            <div>
                <div className="welcome-page-container">
                    <div className="main-title">FuzzyList</div>
                    <div className="secondary-title">Home of strange and irrelevant lists</div>
                    <div className="new-list-container">
                        <div className="" style={{width:"480px"}}>
                            <InputBoxWithButton
                                placeholder="[ Enter you list title here ]"
                                autoFocus={true}
                                inputRef={this.inputBoxRef}
                                errorMessage={this.state.errorMessage}
                                onSubmit={() => {
                                    this.inputBoxRef.current.disabled = true
                                    createNewList(this.inputBoxRef.current.value, this.state.leftToRight, true).catch(reason => {
                                        this.setState({
                                            errorMessage: reason.errorMessage
                                        })
                                        this.inputBoxRef.current.disabled = false
                                        this.inputBoxRef.current.value = ""
                                        this.inputBoxRef.current.focus();
                                    })
                                }}
                            />
                        </div>
                        <div className="list-direction">
                            <RadioGroup
                                name="quiz"
                                value={this.state.leftToRight}
                                onChange={() => this.handleChange(!this.state.leftToRight)}
                            >
                                <FormControlLabel value="true" control={<Radio />} label="Left-to-right" />
                                <FormControlLabel value="false" control={<Radio />} label="Right-to-left  " />
                            </RadioGroup>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
import {Input} from "@mui/material";
import Radio from '@mui/material/Radio';

import React, {RefObject} from "react";
import "./MainPage.css"
import {createNewList} from "../services/ListManagement";

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

    render() {
        return (
            <div>
                <div className="mainPageTitle">
                    <h1>FuzzyList</h1>
                    <h2>Home of strange and irrelevant lists</h2>
                </div>
                <div>
                    <div className="mainPageInputContainer">
                        <div className="inputBoxDiv">
                            <Input style={{width: "400px"}}
                                   placeholder="[ Enter you list title here ]"
                                   autoFocus={true}
                                   inputRef={this.inputBoxRef}
                                   onKeyUp={e => {
                                       if (e.key == 'Enter') {
                                           createNewList(this.inputBoxRef.current.value, this.state.leftToRight, true).catch(reason => {
                                               this.setState({
                                                   errorMessage: reason.errorMessage
                                               })
                                           })
                                           this.inputBoxRef.current.value = ""
                                       }
                                   }}
                            />
                        </div>
                        <div className="iconDiv">
                            <img src="/icons8-add-100.png"
                                 alt="Click to add"
                                 onClick={() => {
                                     createNewList(this.inputBoxRef.current.value, this.state.leftToRight, true).catch(reason => {
                                         this.setState({
                                             errorMessage: reason.errorMessage
                                         })
                                     })
                                     this.inputBoxRef.current.value = ""
                                 }}
                            />
                        </div>
                    </div>
                    <div className="mainPageInputMessage">
                        <div>
                            {this.state.errorMessage}
                        </div>
                    </div>

                    <div className="directionSelectionDiv">
                        <Radio checked={this.state.leftToRight}
                               onChange={() => this.setState({leftToRight: true})}
                               value="leftToRight"
                               name="Left to right"/>
                        <span>Left to right</span>
                        <Radio checked={!this.state.leftToRight}
                               onChange={() => this.setState({leftToRight: false})}
                               value="rightToLeft"
                               name="Right to left"/>
                        <span>Right-to-left</span>

                    </div>
                </div>
            </div>
        )
    }
}
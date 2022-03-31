import React, {MutableRefObject} from "react";
import {getList, ListDataResponse, HeaderResponse, EntryResponse} from "../services/ClientAPI";
import "./ListPage.css"
import {addText} from "../services/ListManagement";
import {withRouter} from "./react-utils";
import {AxiosResponse} from "axios";
import InputBoxWithButton from "../components/InputBoxWithButton";

/**
 * List page state.
 */
interface ListPageState {
    header: HeaderResponse
    entries: EntryResponse[]
}

class ListPageInternal extends React.Component<any, ListPageState> {

    private readonly listKey: string
    private readonly inputBoxRef: MutableRefObject<any>

    /**
     * Class constructor.
     * @param props
     */
    constructor(props: any) {
        super(props);

        // Extract list key. We'll make sure it's valid later on.
        this.listKey = this.props.params.listKey

        // Set initial state.
        this.state = {
            header: {title: '', key: "", leftToRight: true},
            entries: []
        } as ListPageState

        // Create DOM reference, so we can later on access directly to the input element.
        this.inputBoxRef = React.createRef()
    }

    /**
     * After component added to DOM, load entries from server side.
     */
    componentDidMount() {
        this.loadTextEntries().catch(() => {
            alert("Error on loading page.")
        })
    }

    render() {
        return (
            <div className={"list-container " + (this.state.header.leftToRight ? "left-to-right" : "right-to-left")}>
                <div className="back-home-link">
                    <a href="/">&lt;&lt; Home</a>
                </div>
                <div className="list-title">
                    {this.state.header.title}
                </div>
                <div className="entries-wrapper">
                    <div className="add-text-entry">
                        <div className="inputBoxDiv">
                            <InputBoxWithButton style={{width: "400px"}}
                                                placeholder="[ Enter you text here ]"
                                                autoFocus={true}
                                                inputRef={this.inputBoxRef}
                                                onSubmit={() => this.addText()}
                            />
                        </div>
                    </div>
                    <div className="existing-entries-container">
                        {this.state.entries?.map((entry) => {
                            return <div key={"entry_" + entry.index}>{entry.index}. {entry.text}</div>
                        })}
                    </div>

                </div>
            </div>);
    }

    /**
     * Add new text to the list -- submit text entry to the server.
     */
    private addText() {
        addText(this.listKey, this.inputBoxRef.current.value).then(() => this.loadTextEntries())
        this.inputBoxRef.current.value = ""
    }

    /**
     * Load text entries from server into local state.
     *
     * @return Promise of the REST API call.
     */
    private loadTextEntries(): Promise<AxiosResponse<ListDataResponse>> {
        return getList(this.listKey).then(response => {
            this.setState({
                header: response.data.meta,
                entries: response.data.entries
            })
        }) as Promise<AxiosResponse<ListDataResponse>>
    }
}

export const ListPage = withRouter(ListPageInternal)

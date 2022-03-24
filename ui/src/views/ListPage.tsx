import React, {MutableRefObject} from "react";
import {getList, ListDataResponse, HeaderResponse, EntryResponse} from "../services/ClientAPI";
import "./ListPage.css"
import {Input} from "@mui/material";
import {addText} from "../services/ListManagement";
import {withRouter} from "./react-utils";
import {AxiosResponse} from "axios";

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

    componentDidMount() {
        this.loadTextEntries()
    }

    render() {
        return (<div className={"listContainer " + (this.state.header.leftToRight ? "leftToRight" : "rightToLeft")}>
            <h2>
                {this.state.header.title}
            </h2>
            <div className="entriesDiv ">
                <div className="addEntryDiv">
                    <div className="inputBoxDiv">
                        <Input style={{width: "400px"}}
                               placeholder="[ Enter you text here ]"
                               autoFocus={true}
                               inputRef={this.inputBoxRef}
                               onKeyUp={e => {
                                   if (e.key == 'Enter') {
                                       this.addText()
                                   }
                               }}
                        />
                    </div>
                    <div className="iconDiv">
                        <img src="/icons8-add-100.png"
                             alt="Click to add"
                             onClick={() => this.addText()}
                        />
                    </div>
                </div>
                <div className="existingEntriesDiv">
                    {this.state.entries?.map((entry) => {
                        return <div key={"entry_" + entry.index}>{entry.index}. {entry.text}</div>
                    })}
                </div>

            </div>
        </div>);
    }

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

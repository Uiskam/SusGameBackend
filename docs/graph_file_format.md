### Graph File Format

The graph file should be in JSON format and must match the following structure:

#### Nodes

- The `nodes` array contains objects that represent the nodes in the graph.
- Each node object must have a `type` field, which specifies the type of the node: `"Host"`, `"Router"`, or `"Server"`.
- For `Router` nodes, an additional `bufferSize` field is required to specify the buffer size of the router.
- Other features such as `location` can be added, but the parser does not implement them yet.
- Currently number of `Host` nodes must be equal at most to the number of players. This point is going to be fixed soon for ex. by deleting unused hosts and edges utilizing those nodes.

#### Edges

- The `edges` array contains objects that represent the edges in the graph.
- Each edge object must have a `weight` field, which specifies the weight of the edge.
- Each edge object must have a `from` field and a `to` field, which are indices referencing the nodes array.

#### Example

```json
{
  "nodes": [
    {
      "type": "Host"
    },
    (...)
    {
      "type": "Router",
      "bufferSize": 6
    },
    (...)
    {
      "type": "Server"
    }
  ],
  "edges": [
    {
      "weight": 4,
      "from": 0,
      "to": 3
    }
    (...)
  ]
}
```

### Details

- **Node Types**:
    - **Host**: Represents a host node.
    - **Router**: Represents a router node. Currently, requires an additional `bufferSize` field.
    - **Server**: Represents a server node.

- **Edge Fields**:
    - **weight**: An integer representing the weight of the edge.
    - **from**: An integer index referencing the node array, indicating the start node of the edge from node array in this json file.
    - **to**: An integer index analogically referencing the node array, indicating the end node of the edge.

### File Path

- The file path provided to the parser should be a relative path from the `resources` directory of the project. For example, if your graph file is located at `src/main/resources/graph.json`, you should provide the path as `/graph.json`.

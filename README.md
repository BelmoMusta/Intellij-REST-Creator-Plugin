# Intellij-REST-Creator-Plugin

This plugin helps to generate Spring REST controller endpoints by just using the signature of that endpoint.
## Examples
In the intellij idea editor, writing the endpoint signature and press `ctrl + space` ( or whatever key stroke for autocompletion), 
this action will provide an option to create the equivalent REST endpoint.

<table>
<tr>
<td> URL </td> <td> GENERATED METHOD </td>
</tr>
<tr>
<td>

```shell
GET /user/{id}
```

</td>
<td>

```java
    /*
     * Generated from 'GET user/{id}'
     */
    @GetMapping("user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        //FIXME: Provide a valid implementation for this method
        throw new RuntimeException("Not implemented yet");
    } 
```

</td>
</tr>
<tr>
<td> 

```shell
get post/{id}/book/?name=&kk=
```

</td>
<td>

```java
    /*
     * Generated from 'get post/{id}/book/?name=&kk='
     */
    @GetMapping("post/{id}/book/")
    public ResponseEntity<List<?>> getPostBookByPostId(@PathVariable("id") Long postId,
                                                       @RequestParam("name") String name,
                                                       @RequestParam("kk") String kk) {
        //FIXME: Provide a valid implementation for this method
        throw new RuntimeException("Not implemented yet");
    }
```

</td>
</tr>
<tr>
<td>

```shell
POST /book
```

</td>
<td>

```java
    /*
     * Generated from 'POST /book'
     */
    @PostMapping("/book")
    public ResponseEntity<Void> createBook(@RequestBody Object requestBody) {
        //FIXME: Provide a valid implementation for this method
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
```

</td>
</tr>
</table>
The imports section is updated as well, to include introduced references by the created method.
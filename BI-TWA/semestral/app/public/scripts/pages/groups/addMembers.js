(function() {
    const tableE = document.getElementById('table-group-members-add')
    const groupId = tableE.getAttribute('data-group-id')
    const searchE = document.getElementById('search-form-group-users-add')
    const pagingE = document.getElementById('pages-group-members-add')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const addAction = (id, values) => {
        addGroupMembersTable.loader.start()

        fetch(`/api/members`,
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'credentials': 'same-origin'
                },
                body: JSON.stringify(values)
            })
            .then(response => {
                if (response.status === 204){
                    console.log("ADDED")
                }
                addGroupMembersTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                addGroupMembersTable.loader.stop()
            })
    }

    const add = (manager) => {
        return (elem, id) => {
            elem.addEventListener('click', () => {
                const values = {
                    'user': id,
                    'group': groupId,
                    'manager': manager
                }
                addAction(id, values)
            })
        }
    }


    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }


    const addGroupMembersTableBuilder =
        new DataTableBuilder(tableE,`/api/groups/${groupId}/users?show-members=false`, `/group/${groupId}/addUsers`)
            .addField( 'id', 'text', 'Id')

    if(isAuthAdmin){
        addGroupMembersTableBuilder
            .addActionField('name', 'link', 'Name',
                null,
                null,
                detailAction
            )
    }
    else{
        addGroupMembersTableBuilder
            .addField( 'name', 'text', 'Name')
    }

    addGroupMembersTableBuilder
        .addActionField('add-user', 'button', 'Add Member', null, (item) => item["id"], add(false))
        .addActionField('add-manager', 'button', 'Add Manager', null, (item) => item["id"], add(true))

    const addGroupMembersTable = addGroupMembersTableBuilder.build()

    addGroupMembersTable.subscribe(new TablePaging(addGroupMembersTable, pagingE))
    const search = new TableSearch(addGroupMembersTable, searchE, ['name'])
    addGroupMembersTable.subscribe(search)

    //todo: initial state for buttons - addeventlisteners on click
    addGroupMembersTable.content.innerHTML = ''
    addGroupMembersTable.load()
})()
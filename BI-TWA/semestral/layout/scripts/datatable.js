//TODO: 
// 1.) Split update to more methods 
// 2.) Implement fetch

class DataTable{
    constructor(tableElement){
        this.tableElement = tableElement
        this.content = tableE.querySelector('.table-content')
        this.header = tableE.querySelector('.table-header')
        this.content.innerHTML = ''
        this.rows = []
        
        this.selection = []
        this.testData = [
            {
                id: 1,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            },
            {
                id: 2,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            },
            {
                id: 3,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            },
            {
                id: 4,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            },
            {
                id: 5,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            },
            {
                id: 6,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            },
            {
                id: 7,
                name: "John Doe",
                email: "john.doe@gmail.com",
                tel: '+420 123 456 789'
            }
        ]

        this.load()
        this.updateHeader() //param to load after fetch implemented
    }

    load(){
        // Fetch data
        // fetch('url')
        //     .then(response => response.json)
        //     .then(data => {this.update(data)})
        //     .catch(err => {console.log(err.message)})

        this.update()
    }

    update(){
        this.rows = []
        const wrapper = document.createDocumentFragment()
        
        for (let i = 0; i < this.testData.length; i++) {
            const row = document.createElement('article')
            row.classList.add('table-row')
            const ol = document.createElement('ol')
            for (const [key, val] of Object.entries(this.testData[i])) {    
                const li = document.createElement('li')
                li.classList.add(key)
                li.textContent = val

                if(key === 'name'){
                    li.textContent = ''
                    const profileLink = document.createElement('a')
                    profileLink.setAttribute('href', './profile.html')
                    li.append(profileLink)
                    profileLink.textContent = val
                }
                ol.appendChild(li)
            }

            const check = document.createElement('li')
            check.classList.add('check')
            const inputCheck = document.createElement('input')
            inputCheck.classList.add('select-item', 'checkbox')
            inputCheck.setAttribute('type', 'checkbox')
            inputCheck.addEventListener('click', ()=>{
                let found = this.selection.indexOf(this.testData[i].id);

                if(inputCheck.checked && found === -1){
                    this.selection.push(this.testData[i].id)
                }else if(found != -1){
                    this.selection.splice(found, 1)
                }
            })

            check.appendChild(inputCheck)
            ol.appendChild(check)
            row.appendChild(ol)
            wrapper.appendChild(row)
            this.rows.push(inputCheck)
        }

        this.content.appendChild(wrapper)
    }

    updateHeader(){
        this.header.innerHTML = ''
        const ol = document.createElement('ol')  
        for (const [key, val] of Object.entries(this.testData[0])) {  
            const li = document.createElement('li')
            li.classList.add(key)
            li.textContent = key
            ol.appendChild(li)
        }

        const check = document.createElement('li')
        check.classList.add('check')
        const inputCheck = document.createElement('input')
        inputCheck.classList.add('select-item', 'checkbox')
        inputCheck.setAttribute('type', 'checkbox')
        inputCheck.addEventListener('click', ()=>{
            this.rows.forEach((row)=>{
                if(inputCheck.checked ^ row.checked){
                    row.click()
                }
            })
        })
        check.appendChild(inputCheck)
        ol.appendChild(check)
        this.header.appendChild(ol)
    }
}

const tableE = document.getElementById('table-people')
const peopleTable = new DataTable(tableE)

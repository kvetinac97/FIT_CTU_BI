class UserDetail{
    constructor(elem, userId) {
        this.elem = elem
        this.userId = userId
        this.detail = null
    }

    getUserDetail(){
        fetch(`/api/users/${this.userId}`,
            {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'credentials': 'same-origin'
                }})
            .then(response => {
                console.log(response)
                return response.json()
            })
            .then(data => { this.parseData(data) })
            .catch(err => {
                console.log(err.message)
            })
    }

    parseData(user) {
        this.detail.innerHTML =
            `
            <div class="detail-box-image">
                <img src="/uploads/${user.image}" alt="Profile picture">
            </div>
            <div class="detail-box-info">
                <h2>Profile</h2>
                <dl>
                    <dt>Name</dt>
                    <dd>${user.name}</dd>
                    <dt>Email</dt>
                    <dd>${user.email}</dd>
                    <dt>Phone</dt>
                    <dd>${user.tel}</dd>
                    <dt>Roles</dt>
                    <dd>
                        <ul class="list">
            `
            +
            user["roles"].map(role => `<li>${role}</li>`).join('')
            +
            `
                           
                        </ul>
                    </dd>
                </dl>
            </div>
        `

        console.log(user)
    }


    detailAction() {
        if(this.userId === null)
            return

        this.detail = document.createElement('div')
        let opened = false
        let loaded = false

        this.elem.setAttribute('href', `/user/${this.userId}`)
        this.elem.addEventListener('mouseenter', (e) => {
            if (window.innerWidth > 900 && !opened) {
                this.detail.classList.add('detail-box')
                if (!loaded){
                    this.detail.textContent = "LOADING..."
                    this.getUserDetail()
                    loaded = true
                }
                this.elem.append(this.detail)
                opened = true
            }
        })

        this.elem.addEventListener('mouseleave', (e) => {
            if (opened){
                opened = false
                this.detail.remove()
            }
        })
    }

}
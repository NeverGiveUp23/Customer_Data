import UserProfile from './UserProfile.jsx';
import React, {useState, useEffect} from 'react'



const users = [
    {
        name: 'Jasmin',
        age: 22,
        gender: "FEMALE"
    },
    {
        name: 'Ana',
        age: 32,
        gender: "FEMALE"
    },
    {
        name: 'Alex',
        age: 45,
        gender: "MALE"
    },
    {
        name: 'Mike',
        age: 82,
        gender: "MALE"
    },
]

const UserProfiles = ({users}) => (
    <div>
        {users.map((user, index) => (

            <UserProfile key={index}
                         name={user.name}
                         age={user.age}
                         gender={user.gender}
                         imageNumber={index}
            />
        ))}

    </div>
)


function App() {


    const [counter, setCounter] = useState(0)
    const [isLoading, setIsLoading] = useState(false)
    const [showToast, setShowToast] = useState(false)

    useEffect(() => {
        setIsLoading(true)
        setTimeout(() => {
            setIsLoading(false)
        }, 3000)
    }, [])

    if(isLoading){
        return <div className={"text-center"}>
            <h3 className={" spinner-border text-primary"}></h3>
        </div>
    }

    // if(showToast) {
    //     return <div className={"toast"} role={"alert"} aria-live={"assertive"} aria-atomic={"true"}>
    //         <div className={"toast-header"}>
    //             <img src={"..."} className={"rounded me-2"} alt={"..."}/>
    //             <strong className={"me-auto"}>Bootstrap</strong>
    //             <small>11 mins ago</small>
    //             <button type={"button"} className={"btn-close"} data-bs-dismiss={"toast"} aria-label={"Close"}></button>
    //         </div>
    //         <div className={"toast-body"}>
    //             Hello, world! This is a toast message.
    //         </div>
    //     </div>
    // }


    if(counter === -1){
        setCounter(0)
    }


    return (
        <React.Fragment>

    <button  className={"btn btn-primary"}
             onClick={() => setCounter(counter + 1)} >

        Increment Counter
    </button>
    <button className={"btn btn-warning"}
            onClick={() => setCounter(counter - 1)}>

        Decrement Counter
    </button>
    <button className={"btn btn-danger"}
            onClick={() => setCounter(0)}>

        Restart
    </button>
    <h1>{counter}</h1>
    <UserProfiles users={users}/>
        </React.Fragment>
    )
}

export default App

const UserProfile = ({name, age, gender, imageNumber, ...props}) => {

    gender = gender === "MALE" ? "men" : "women"


    return (
        <div>
            <p>{name}</p>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`} alt="user" />

            {props.children}
        </div>
    )
}

export default UserProfile;



import SideBarWithHeader from './components/shared/SideBar.jsx';
import {Text} from "@chakra-ui/react";
const Home = ()  => {


    return (
        <SideBarWithHeader justify={"center"} align={"center"}>


            <Text fontSize={"5xl"} >Dashboard</Text>

        </SideBarWithHeader>
    )
}

export default Home;

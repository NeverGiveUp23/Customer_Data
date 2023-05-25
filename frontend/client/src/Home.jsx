
import SideBarWithHeader from './components/shared/SideBar.jsx';
import {Text} from "@chakra-ui/react";
import {useDisclosure} from "@chakra-ui/react";
import {Button, Box, ScaleFade} from "@chakra-ui/react";
const Home = ()  => {

    function ScaleFadeEx() {
        const { isOpen, onToggle } = useDisclosure()

        return (
            <>
                <Button onClick={onToggle}>Click Me</Button>
                <ScaleFade initialScale={0.9} in={isOpen}>
                    <Box
                        p='40px'
                        color='white'
                        mt='4'
                        bg='teal.500'
                        rounded='md'
                        shadow='md'
                    >
                        Fade
                    </Box>
                </ScaleFade>
            </>
        )
    }


    return (
        <SideBarWithHeader justify={"center"} align={"center"}>

            <h1>Hello</h1>



        </SideBarWithHeader>
    )
}

export default Home;

import { Wrap, WrapItem, Spinner, Text, Center } from '@chakra-ui/react';
import SideBarWithHeader from './components/shared/SideBar.jsx';
import {useEffect, useState} from 'react';
import {getCustomers} from './services/client.js';
import CardWithImage from './components/Card';

const App = ()  => {
    const [ customers, setCustomers] = useState([]);
    const [ isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true)
        setTimeout(() => {
            getCustomers().then((res) => {
                setCustomers(res.data);
            }).catch((err) => {
                console.log(err);
            }).finally(() => {
                setIsLoading(false);
            })
        }, 3000);
    }, []);

        if(isLoading){
          return ( <SideBarWithHeader>
              <Center h={"500px"}>
                  <Spinner
                      size='xl'
                      speed='0.65s'
                      emptyColor='gray.200'
                      color='blue.500'
                  />
              </Center>

            </SideBarWithHeader>)
        }

        if(customers.length <= 0){
            return (
                <SideBarWithHeader>
                    <Center h={"500px"}>
                        <Text fontSize={"4xl"}>No Customers Found!</Text>
                    </Center>
                </SideBarWithHeader>
            )
        }

    return (
        <SideBarWithHeader justify={"center"} align={"center"}>
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...customer}
                            imageNumber={index}
                        />
                    </WrapItem>

                ))}
            </Wrap>

        </SideBarWithHeader>
    )
}

export default App;

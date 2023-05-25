

import React from 'react';
import {
    Avatar,
    Box,
    CloseButton,
    Drawer,
    DrawerContent,
    Flex,
    HStack,
    Icon,
    IconButton,
    Link,
    Menu,
    MenuButton,
    MenuDivider,
    MenuItem,
    MenuList,
    Text,
    useColorModeValue,
    useDisclosure,
    VStack,
    Image,
    Button,
    Badge,
    ScaleFade,
} from '@chakra-ui/react';
import {useAuth} from "../context/AuthContext.jsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {getCustomerProfilePictureUrl} from "../../services/client.jsx";
import {
    FiBell,
    FiChevronDown,
    FiHome,
    FiMenu,
    FiSettings,FiUsers, FiBriefcase
} from 'react-icons/fi';

const LinkItems = [
    {name: 'Company', route:'/dashboard', icon: FiBriefcase},
    {name: 'Customers', route:'/dashboard/customers', icon: FiUsers},
    {name: 'Settings', route:'/dashboard/settings', icon: FiSettings},
];

export default function SidebarWithHeader({ customers, children}) {
    const {isOpen, onOpen, onClose} = useDisclosure();


    return (
        <Box minH="100vh" bg={useColorModeValue('gray.100', 'gray.900')}>
            <SidebarContent
                onClose={() => onClose}
                display={{base: 'none', md: 'block'}}
            />
            <Drawer
                autoFocus={false}
                isOpen={isOpen}
                placement="left"
                onClose={onClose}
                returnFocusOnClose={false}
                onOverlayClick={onClose}
                size="full">
                <DrawerContent>
                    <SidebarContent onClose={onClose}/>
                </DrawerContent>
            </Drawer>
            {/* mobilenav */}

            <MobileNav
                customers = {customers}
                onOpen={onOpen}/>
            <Box ml={{base: 0, md: 60}} p="4">
                {children}
            </Box>
        </Box>
    );
}

const SidebarContent = ({ onClose, ...rest}) => {


    return (
                <Box
                    transition="3s ease"
                    bg={useColorModeValue('white', 'gray.900')}
                    borderRight="1px"
                    borderRightColor={useColorModeValue('gray.200', 'gray.700')}
                    w={{base: 'full', md: 60}}
                    pos="fixed"
                    h="full"
                    {...rest}>
                    <Flex h="20" flexDirection="column" alignItems="center" mx="8" mb={75} mt={2} justifyContent="space-between">
                        <Text fontSize="2xl" fontFamily="monospace" fontWeight="bold" mb={5}>
                            Dashboard
                        </Text>
                        <CloseButton display={{base: 'flex', md: 'none'}} onClick={onClose}/>
                    </Flex>

                    {LinkItems.map((link) => (
                        link.name === 'Company' ? (

                                    <NavItem key={link.name} route={link.route} icon={link.icon}>
                                        {link.name}
                                        <Badge key={link.name}
                                               ml={2}
                                               variant='solid' colorScheme='green'> Coming Soon</Badge>
                                    </NavItem>
                            ) : (

                            <NavItem key={link.name} route={link.route} icon={link.icon}>
                                {link.name}
                            </NavItem>
                            )
                    ))}
                    </Box>

    );
};

const NavItem = ({icon, route, children, ...rest}) => {
    return (
        <Link href={route} style={{textDecoration: 'none'}} _focus={{boxShadow: 'none'}}>



            <Flex
                align="center"
                p="4"
                mx="4"
                borderRadius="lg"
                role="group"
                cursor="pointer"
                _hover={{
                    bg: 'facebook.400',
                    color: 'white',
                }}

                {...rest}>
                {icon && (
                    <Icon
                        mr="4"
                        fontSize="16"
                        _groupHover={{
                            color: 'white',
                        }}
                        as={icon}
                    />
                )}
                {children}
            </Flex>
         </Link>
    );
};

const MobileNav = ({onOpen, ...rest}) => {
    const {logOut, customer} = useAuth();
    const navigate = useNavigate();

    return (
        <Flex
            ml={{base: 0, md: 60}}
            px={{base: 4, md: 4}}
            height="20"
            alignItems="center"
            bg={useColorModeValue('white', 'gray.900')}
            borderBottomWidth="1px"
            borderBottomColor={useColorModeValue('gray.200', 'gray.700')}
            justifyContent={{base: 'space-between', md: 'flex-end'}}
            {...rest}>
            <IconButton
                display={{base: 'flex', md: 'none'}}
                onClick={onOpen}
                variant="outline"
                aria-label="open menu"
                icon={<FiMenu/>}
            />

            <Text
                display={{base: 'flex', md: 'none'}}
                fontSize="2xl"
                fontFamily="monospace"
                fontWeight="bold">
                Logo
            </Text>

            <HStack spacing={{base: '0', md: '6'}}>
                <IconButton
                    size="lg"
                    variant="ghost"
                    aria-label="open menu"
                    icon={<FiBell/>}
                />
                <Flex alignItems={'center'}>
                    <Menu>
                        <MenuButton
                            py={2}
                            transition="all 0.3s"
                            _focus={{boxShadow: 'none'}}>
                            <HStack>

                                <VStack
                                    display={{base: 'none', md: 'flex'}}
                                    alignItems="flex-start"
                                    spacing="1px"
                                    ml="2">

                                        <Text fontSize="sm">{customer?.username}</Text>


                                </VStack>


                                <Box display={{base: 'none', md: 'flex'}}>
                                    <FiChevronDown/>
                                </Box>
                            </HStack>
                        </MenuButton>
                        <MenuList
                            bg={useColorModeValue('white', 'gray.900')}
                            borderColor={useColorModeValue('gray.200', 'gray.700')}>
                            <MenuItem>Profile</MenuItem>
                            <MenuItem>Settings</MenuItem>
                            <MenuItem>Billing</MenuItem>
                            <MenuDivider/>
                            <MenuItem
                            onClick={logOut}
                            >Sign out</MenuItem>
                        </MenuList>
                    </Menu>
                </Flex>
            </HStack>
        </Flex>
    );
};

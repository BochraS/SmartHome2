export interface IProfile {
    id?: number;
    lastName?: string;
    firstName?: string;
    age?: number;
    userLogin?: string;
    userId?: number;
    profileConfValueId?: number;
}

export class Profile implements IProfile {
    constructor(
        public id?: number,
        public lastName?: string,
        public firstName?: string,
        public age?: number,
        public userLogin?: string,
        public userId?: number,
        public profileConfValueId?: number
    ) {}
}

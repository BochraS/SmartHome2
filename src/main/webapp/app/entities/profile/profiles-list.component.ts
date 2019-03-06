import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProfile } from 'app/shared/model/profile.model';
import { AccountService } from 'app/core';
import { ProfileService } from './profile.service';

@Component({
  selector: 'jhi-profiles-list',
  templateUrl: './profiles-list.component.html',
  styleUrls: ['./profiles-list.component.css']
})
export class ProfilesListComponent implements OnInit {

    profiles: IProfile[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected profileService: ProfileService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }
    OnProfileClick()
    {

    }
    loadAll() {
        if (this.currentSearch) {
            this.profileService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IProfile[]>) => (this.profiles = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.profileService.query().subscribe(
            (res: HttpResponse<IProfile[]>) => {
                this.profiles = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProfiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProfile) {
        return item.id;
    }

    registerChangeInProfiles() {
        this.eventSubscriber = this.eventManager.subscribe('profileListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

}
